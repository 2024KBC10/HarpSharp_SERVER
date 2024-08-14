package com.harpsharp.todo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.auth.service.RefreshTokenService;
import com.harpsharp.auth.service.UserService;
import com.harpsharp.infra_rds.dto.board.*;
import com.harpsharp.infra_rds.dto.todo.*;
import com.harpsharp.infra_rds.dto.user.JoinTestDTO;
import com.harpsharp.infra_rds.dto.user.LoginDTO;
import com.harpsharp.infra_rds.util.TodoStatus;
import com.harpsharp.todo.service.TodoCommentService;
import com.harpsharp.todo.service.TodoPostService;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.DefaultErrorViewResolver;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@ActiveProfiles("local")
@ComponentScan(basePackages = {"com.harpsharp.todo", "com.harpsharp.infra_rds"})
class TodoApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RefreshTokenService refreshTokenService;
	@Autowired
	private UserService userService;

	private final String username = "admin";
	private final String password = "HeisPassWord!15";
	private final String email    = "admin@gmail.com";
	private String accessToken    = "EMPTY";
	private Cookie refreshToken   = null;

	private final String title   = "test!";
	private final String content = "blahblah!";
	private final String content_hint = "thisishint";
	private final String content_goal = "thisisgoal";
	private final TodoStatus status = TodoStatus.RUNNING;
	private final LocalDateTime startAt = LocalDateTime.now();
	private final LocalDateTime endAt = LocalDateTime.now().plusDays(1);

	@Autowired
	private TodoPostService postService;
	@Autowired
	private TodoCommentService commentService;

	Map<Long, ResponseTodoPostDTO> 	  post = null;
	Map<Long, ResponseTodoCommentDTO> comment = null;
    @Autowired
    private DefaultErrorViewResolver conventionErrorViewResolver;

	public void init() throws Exception{
		JoinTestDTO joinDTO = new JoinTestDTO(username, password, email);

		String joinJson = objectMapper.writeValueAsString(joinDTO);


		this.mockMvc.perform(
						post("/join")
								.contentType(MediaType.APPLICATION_JSON)
								.content(joinJson))
				.andReturn();
	}
	public void login() throws Exception {
		init();

		LoginDTO loginDto = new LoginDTO(username, password);

		String loginJson = objectMapper.writeValueAsString(loginDto);

		MvcResult result = this.mockMvc.perform(
						post("/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(loginJson))
				.andExpect(status().isOk())
				.andReturn();

		accessToken  = result.getResponse().getHeader("Authorization").split(" ")[1];
		refreshToken = result.getResponse().getCookie("refresh");
	}

	public Long writePost() throws Exception{
		RequestTodoPostDTO requestPostDTO = new RequestTodoPostDTO(
				username,
				title,
				content,
				status,
				startAt,
				endAt);

		String postJson = objectMapper.writeValueAsString(requestPostDTO);

		MvcResult result = this.mockMvc.perform(
						post("/todo/posts")
								.header("Authorization", "Bearer " + accessToken)
								.contentType(MediaType.APPLICATION_JSON)
								.content(postJson))
				.andExpect(status().isOk())
				.andReturn();

		String responseContent = result.getResponse().getContentAsString();
		Map<String, Object> jsonResponse = objectMapper.readValue(responseContent, new TypeReference<Map<String, Object>>() {});
		Map<String, Object> dataMap = (Map<String, Object>) jsonResponse.get("data");

		Map.Entry<String, Object> firstEntry = dataMap.entrySet().iterator().next();

		return Long.parseLong(firstEntry.getKey());
	}

	public List<Long> writePC() throws Exception{
		RequestTodoPostDTO requestPostDTO = new RequestTodoPostDTO(
				username,
				title,
				content,
				status,
				startAt,
				endAt);
		String postJson = objectMapper.writeValueAsString(requestPostDTO);

		MvcResult result = this.mockMvc.perform(
						post("/todo/posts")
								.header("Authorization", "Bearer " + accessToken)
								.contentType(MediaType.APPLICATION_JSON)
								.content(postJson))
				.andExpect(status().isOk())
				.andReturn();

		String responseContent = result.getResponse().getContentAsString();
		Map<String, Object> jsonResponse = objectMapper.readValue(responseContent, new TypeReference<Map<String, Object>>() {});
		Map<String, Object> dataMap = (Map<String, Object>) jsonResponse.get("data");

		Map.Entry<String, Object> firstEntry = dataMap.entrySet().iterator().next();
		Long postId = Long.parseLong(firstEntry.getKey());

		RequestTodoCommentDTO requestCommentDTO = new RequestTodoCommentDTO(postId, username, content);
		String commentJson = objectMapper.writeValueAsString(requestCommentDTO);

		MvcResult resultComment = this.mockMvc
				.perform(
						post("/todo/posts/comments")
								.header("Authorization", "Bearer " + accessToken)
								.contentType(MediaType.APPLICATION_JSON)
								.content(commentJson))
				.andExpect(status().isOk())
				.andReturn();

		responseContent = resultComment.getResponse().getContentAsString();
		jsonResponse = objectMapper.readValue(responseContent, new TypeReference<Map<String, Object>>() {});
		dataMap = (Map<String, Object>) jsonResponse.get("data");

		firstEntry = dataMap.entrySet().iterator().next();
		Long commentId = Long.parseLong(firstEntry.getKey());

		return List.of(postId, commentId);
	}

	@BeforeEach
	public void setUp() throws Exception {
		login();
	}

	@AfterEach
	public void tearDown() throws Exception {
		commentService.clear();
		postService.clear();
		userService.clear();
		refreshTokenService.clear();
	}

	@DisplayName("TODO 루트 페이지")
	@Test
	@Transactional
	public void rootPage() throws Exception {
		this.mockMvc.perform(get("/todo"))
				.andExpect(status().isOk())
				.andDo(document("Root Page", // 문서화할 때 사용할 경로와 이름
						responseFields(
								fieldWithPath("timeStamp")
										.type(JsonFieldType.STRING)
										.description("응답 시간"),
								fieldWithPath("code")
										.type(JsonFieldType.VARIES)
										.description("상태 코드"),
								fieldWithPath("message")
										.type(JsonFieldType.STRING)
										.description("응답 성공 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지")
						)
				));
	}

	@DisplayName("TODO 작성 테스트")
	@Test
	@Transactional
	public void writeTest() throws Exception {
		RequestTodoPostDTO postDTO = new RequestTodoPostDTO(
				username,
				title,
				content,
				status,
				startAt,
				endAt);

		this.mockMvc.perform(
						post("/todo/posts")
								.header("Authorization", "Bearer " + accessToken)
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(postDTO)))
				.andExpect(status().isOk())
				.andDo(document("Post", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("username").description("작성자"),
								fieldWithPath("title").description("제목"),
								fieldWithPath("content").description("내용"),
								fieldWithPath("status").description("진행 상태"),
								fieldWithPath("startAt").description("시작 시간"),
								fieldWithPath("endAt").description("마감 시간")
						),
						requestHeaders(headerWithName("Authorization").description("유효한 access token")),
						responseFields(
								fieldWithPath("timeStamp")
										.type(JsonFieldType.STRING)
										.description("응답 시간"),
								fieldWithPath("code")
										.type(JsonFieldType.VARIES)
										.description("상태 코드"),
								fieldWithPath("message")
										.type(JsonFieldType.STRING)
										.description("작성 성공 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지"),
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("전체 게시글 정보"),
								fieldWithPath("data.*.username").type(JsonFieldType.STRING).description("작성자 이름"),
								fieldWithPath("data.*.title").type(JsonFieldType.STRING).description("게시글 제목"),
								fieldWithPath("data.*.content").type(JsonFieldType.STRING).description("게시글 내용"),
								fieldWithPath("data.*.status").type(JsonFieldType.STRING).description("작성 일자"),
								fieldWithPath("data.*.startAt").type(JsonFieldType.STRING).description("작성 일자"),
								fieldWithPath("data.*.endAt").type(JsonFieldType.STRING).description("수정 일자"),
								fieldWithPath("data.*.createdAt").type(JsonFieldType.STRING).description("작성 일자"),
								fieldWithPath("data.*.updatedAt").type(JsonFieldType.STRING).description("수정 일자"),
								fieldWithPath("data.*.likes").type(JsonFieldType.VARIES).description("수정 일자"),
								fieldWithPath("data.*.comments").type(JsonFieldType.OBJECT).description("댓글 정보")
						)
				));

	}



	@DisplayName("전체 TODO 조회 테스트")
	@Test
	@Transactional
	public void getAllPostsTest() throws Exception {
		writePost();
		this.mockMvc.perform(get("/todo/posts"))
				.andExpect(status().isOk())
				.andDo(document("Get All Todo Posts", // 문서화할 때 사용할 경로와 이름
						responseFields(
								fieldWithPath("timeStamp")
										.type(JsonFieldType.STRING)
										.description("응답 시간"),
								fieldWithPath("code")
										.type(JsonFieldType.VARIES)
										.description("상태 코드"),
								fieldWithPath("message")
										.type(JsonFieldType.STRING)
										.description("조회 성공 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지"),
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("전체 게시글 정보"),
								fieldWithPath("data.*.username").type(JsonFieldType.STRING).description("작성자 이름"),
								fieldWithPath("data.*.title").type(JsonFieldType.STRING).description("게시글 제목"),
								fieldWithPath("data.*.content").type(JsonFieldType.STRING).description("게시글 내용"),
								fieldWithPath("data.*.status").type(JsonFieldType.STRING).description("작성 일자"),
								fieldWithPath("data.*.startAt").type(JsonFieldType.STRING).description("작성 일자"),
								fieldWithPath("data.*.endAt").type(JsonFieldType.STRING).description("수정 일자"),
								fieldWithPath("data.*.createdAt").type(JsonFieldType.STRING).description("작성 일자"),
								fieldWithPath("data.*.updatedAt").type(JsonFieldType.STRING).description("수정 일자"),
								fieldWithPath("data.*.likes").type(JsonFieldType.VARIES).description("수정 일자"),
								fieldWithPath("data.*.comments").type(JsonFieldType.OBJECT).description("댓글 정보")
						)
				));
	}

	@DisplayName("단일 TODO 조회 테스트")
	@Test
	@Transactional
	public void getPostByPostId() throws Exception {
		Long postId = writePost();
		this.mockMvc.perform(get("/todo/posts/{postId}", postId))
				.andExpect(status().isOk())
				.andDo(
						document("Get Todo Post by todoPostId",
								pathParameters(parameterWithName("postId").description("게시글 ID")),
								responseFields(
										fieldWithPath("timeStamp")
												.type(JsonFieldType.STRING)
												.description("응답 시간"),
										fieldWithPath("code")
												.type(JsonFieldType.VARIES)
												.description("상태 코드"),
										fieldWithPath("message")
												.type(JsonFieldType.STRING)
												.description("조회 성공 여부"),
										fieldWithPath("details")
												.type(JsonFieldType.STRING)
												.description("상세 메세지"),
										fieldWithPath("data").type(JsonFieldType.OBJECT).description("전체 게시글 정보"),
										fieldWithPath("data.*.username").type(JsonFieldType.STRING).description("작성자 이름"),
										fieldWithPath("data.*.title").type(JsonFieldType.STRING).description("게시글 제목"),
										fieldWithPath("data.*.content").type(JsonFieldType.STRING).description("게시글 내용"),
										fieldWithPath("data.*.status").type(JsonFieldType.STRING).description("작성 일자"),
										fieldWithPath("data.*.startAt").type(JsonFieldType.STRING).description("작성 일자"),
										fieldWithPath("data.*.endAt").type(JsonFieldType.STRING).description("수정 일자"),
										fieldWithPath("data.*.createdAt").type(JsonFieldType.STRING).description("작성 일자"),
										fieldWithPath("data.*.updatedAt").type(JsonFieldType.STRING).description("수정 일자"),
										fieldWithPath("data.*.likes").type(JsonFieldType.VARIES).description("수정 일자"),
										fieldWithPath("data.*.comments").type(JsonFieldType.OBJECT).description("댓글 정보")
								)));
	}


	@DisplayName("TODO 수정 테스트")
	@Test
	@Transactional
	public void updatePost() throws Exception {
		Long postId = writePost();
		RequestUpdateTodoPostDTO requestPostDTO = new RequestUpdateTodoPostDTO(
				postId,
				username,
				"Modified!",
				content,
				status,
				startAt,
				endAt);

		this.mockMvc.perform(patch("/todo/posts")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(requestPostDTO)))
				.andExpect(status().isOk())
				.andDo(document("Update Todo Post",
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("postId").description("postId"),
								fieldWithPath("username").description("작성자"),
								fieldWithPath("title").description("제목"),
								fieldWithPath("content").description("내용"),
								fieldWithPath("status").description("진행 상태"),
								fieldWithPath("startAt").description("시작 시간"),
								fieldWithPath("endAt").description("마감 시간")
						),
						requestHeaders(headerWithName("Authorization").description("유효한 access token")),
						responseFields(
								fieldWithPath("timeStamp")
										.type(JsonFieldType.STRING)
										.description("응답 시간"),
								fieldWithPath("code")
										.type(JsonFieldType.VARIES)
										.description("상태 코드"),
								fieldWithPath("message")
										.type(JsonFieldType.STRING)
										.description("수정 성공 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지"),
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("전체 게시글 정보"),
								fieldWithPath("data.*.username").type(JsonFieldType.STRING).description("작성자 이름"),
								fieldWithPath("data.*.title").type(JsonFieldType.STRING).description("게시글 제목"),
								fieldWithPath("data.*.content").type(JsonFieldType.STRING).description("게시글 내용"),
								fieldWithPath("data.*.status").type(JsonFieldType.STRING).description("작성 일자"),
								fieldWithPath("data.*.startAt").type(JsonFieldType.STRING).description("작성 일자"),
								fieldWithPath("data.*.endAt").type(JsonFieldType.STRING).description("수정 일자"),
								fieldWithPath("data.*.createdAt").type(JsonFieldType.STRING).description("작성 일자"),
								fieldWithPath("data.*.updatedAt").type(JsonFieldType.STRING).description("수정 일자"),
								fieldWithPath("data.*.likes").type(JsonFieldType.VARIES).description("수정 일자"),
								fieldWithPath("data.*.comments").type(JsonFieldType.OBJECT).description("댓글 정보")
						)
				));
	}


	@DisplayName("TODO 삭제 테스트")
	@Test
	@Transactional
	public void deletePost() throws Exception {
		Long postId = writePost();
		RequestUpdateTodoPostDTO requestPostDTO = new RequestUpdateTodoPostDTO(
				postId,
				username,
				"Modified!",
				content,
				status,
				startAt,
				endAt);
		this.mockMvc.perform(delete("/todo/posts")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(requestPostDTO)))
				.andExpect(status().isOk())
				.andDo(document("Delete Todo Post", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("postId").description("todoPostId"),
								fieldWithPath("username").description("작성자"),
								fieldWithPath("title").description("제목"),
								fieldWithPath("content").description("내용"),
								fieldWithPath("status").description("진행 상태"),
								fieldWithPath("startAt").description("시작 시간"),
								fieldWithPath("endAt").description("마감 시간")
						),
						requestHeaders(headerWithName("Authorization").description("유효한 access token")),
						responseFields(
								fieldWithPath("timeStamp")
										.type(JsonFieldType.STRING)
										.description("응답 시간"),
								fieldWithPath("code")
										.type(JsonFieldType.VARIES)
										.description("상태 코드"),
								fieldWithPath("message")
										.type(JsonFieldType.STRING)
										.description("삭제 성공 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지")
						)
				));
	}




	@DisplayName("TODO 댓글 작성 테스트")
	@Test
	@Transactional
	public void writeComment() throws Exception {
		Long postId = writePost();
		RequestTodoCommentDTO commentDTO = new RequestTodoCommentDTO(postId, username, content);

		this.mockMvc.perform(
						post("/todo/posts/comments")
								.header("Authorization", "Bearer " + accessToken)
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(commentDTO)))
				.andExpect(status().isOk())
				.andDo(document("Write Todo Comment", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("postId").description("Todo Post ID"),
								fieldWithPath("username").description("작성자"),
								fieldWithPath("content").description("내용")
						),
						requestHeaders(headerWithName("Authorization").description("유효한 access token")),
						responseFields(
								fieldWithPath("timeStamp")
										.type(JsonFieldType.STRING)
										.description("응답 시간"),
								fieldWithPath("code")
										.type(JsonFieldType.VARIES)
										.description("상태 코드"),
								fieldWithPath("message")
										.type(JsonFieldType.STRING)
										.description("작성 성공 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지"),
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("작성한 댓글 정보"),
								fieldWithPath("data.*.username").type(JsonFieldType.STRING).description("작성자 이름"),
								fieldWithPath("data.*.content").type(JsonFieldType.STRING).description("게시글 내용"),
								fieldWithPath("data.*.createdAt").type(JsonFieldType.STRING).description("작성 일자"),
								fieldWithPath("data.*.updatedAt").type(JsonFieldType.STRING).description("수정 일자")
						)
				));

	}

	@DisplayName("단일 TODO 댓글 조회 테스트")
	@Test
	@Transactional
	public void getCommentByCommentId() throws Exception {
		List<Long> list_id = writePC();
		Long postId    = list_id.get(0);
		Long commentId = list_id.get(1);
		System.out.println("postId = " + postId);
		System.out.println("commentId = " + commentId);
		this.mockMvc.perform(get("/todo/posts/{postId}/comments/{commentId}", postId, commentId))
				.andExpect(status().isOk())
				.andDo(document("Get Todo Comment by commentId", // 문서화할 때 사용할 경로와 이름
						pathParameters(parameterWithName("postId").description("게시글 Id"),
								parameterWithName("commentId").description("댓글 ID")),
						responseFields(
								fieldWithPath("timeStamp")
										.type(JsonFieldType.STRING)
										.description("응답 시간"),
								fieldWithPath("code")
										.type(JsonFieldType.VARIES)
										.description("상태 코드"),
								fieldWithPath("message")
										.type(JsonFieldType.STRING)
										.description("조회 성공 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지"),
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("단일 댓글 정보"),
								fieldWithPath("data.*.username").type(JsonFieldType.STRING).description("작성자 이름"),
								fieldWithPath("data.*.content").type(JsonFieldType.STRING).description("게시글 내용"),
								fieldWithPath("data.*.createdAt").type(JsonFieldType.STRING).description("작성 일자"),
								fieldWithPath("data.*.updatedAt").type(JsonFieldType.STRING).description("수정 일자")
						)
				));
	}

	@DisplayName("TODO 댓글 조회 테스트")
	@Test
	@Transactional
	public void getCommentsByPostId() throws Exception {
		List<Long> list_id = writePC();
		Long postId    = list_id.get(0);
		this.mockMvc.perform(get("/todo/posts/{postId}/comments", postId))
				.andExpect(status().isOk())
				.andDo(
						document("Get Comments by todoPostId",
								pathParameters(parameterWithName("postId").description("게시글 ID")),
								responseFields(
										fieldWithPath("timeStamp")
												.type(JsonFieldType.STRING)
												.description("응답 시간"),
										fieldWithPath("code")
												.type(JsonFieldType.VARIES)
												.description("상태 코드"),
										fieldWithPath("message")
												.type(JsonFieldType.STRING)
												.description("조회 성공 여부"),
										fieldWithPath("details")
												.type(JsonFieldType.STRING)
												.description("상세 메세지"),
										fieldWithPath("data").type(JsonFieldType.OBJECT).description("전체 댓글 정보"),
										fieldWithPath("data.*.username").type(JsonFieldType.STRING).description("작성자 이름"),
										fieldWithPath("data.*.content").type(JsonFieldType.STRING).description("게시글 내용"),
										fieldWithPath("data.*.createdAt").type(JsonFieldType.STRING).description("작성 일자"),
										fieldWithPath("data.*.updatedAt").type(JsonFieldType.STRING).description("수정 일자")
								)));
	}


	@DisplayName("TODO 댓글 수정 테스트")
	@Test
	@Transactional
	public void updateComment() throws Exception {
		List<Long> list_id = writePC();
		Long commentId = list_id.get(1);
		RequestUpdateTodoCommentDTO commentDTO = new RequestUpdateTodoCommentDTO(commentId, username, "Modified!");
		this.mockMvc.perform(patch("/todo/posts/comments")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(commentDTO)))
				.andExpect(status().isOk())
				.andDo(document("Update Todo Comment",
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("commentId").description("Comment 식별자"),
								fieldWithPath("username").description("작성자"),
								fieldWithPath("content").description("내용 (if null: 변경 X)")
						),
						requestHeaders(headerWithName("Authorization").description("유효한 access token")),
						responseFields(
								fieldWithPath("timeStamp")
										.type(JsonFieldType.STRING)
										.description("응답 시간"),
								fieldWithPath("code")
										.type(JsonFieldType.VARIES)
										.description("상태 코드"),
								fieldWithPath("message")
										.type(JsonFieldType.STRING)
										.description("수정 성공 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지"),
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("수정한 댓글 정보"),
								fieldWithPath("data.*.username").type(JsonFieldType.STRING).description("작성자 이름"),
								fieldWithPath("data.*.content").type(JsonFieldType.STRING).description("게시글 내용"),
								fieldWithPath("data.*.createdAt").type(JsonFieldType.STRING).description("작성 일자"),
								fieldWithPath("data.*.updatedAt").type(JsonFieldType.STRING).description("수정 일자")
						)
				));
	}


	@DisplayName("TODO 댓글 삭제 테스트")
	@Test
	@Transactional
	public void deleteComment() throws Exception {
		List<Long> list_id = writePC();
		Long commentId = list_id.get(1);
		RequestUpdateTodoCommentDTO commentDTO = new RequestUpdateTodoCommentDTO(commentId, username, content);
		this.mockMvc.perform(delete("/todo/posts/comments")
						.header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(commentDTO)))
				.andExpect(status().isOk())
				.andDo(document("Delete Todo Comment", // 문서화할 때 사용할 경로와 이름
						requestHeaders(headerWithName("Authorization").description("유효한 access token")),
						responseFields(
								fieldWithPath("timeStamp")
										.type(JsonFieldType.STRING)
										.description("응답 시간"),
								fieldWithPath("code")
										.type(JsonFieldType.VARIES)
										.description("상태 코드"),
								fieldWithPath("message")
										.type(JsonFieldType.STRING)
										.description("삭제 성공 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지")
						)
				));
	}

}
