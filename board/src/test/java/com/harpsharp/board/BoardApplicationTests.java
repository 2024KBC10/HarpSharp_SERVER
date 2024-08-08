package com.harpsharp.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.auth.service.RefreshTokenService;
import com.harpsharp.auth.service.UserService;
import com.harpsharp.board.service.CommentService;
import com.harpsharp.board.service.PostService;
import com.harpsharp.infra_rds.dto.board.RequestCommentDTO;
import com.harpsharp.infra_rds.dto.board.RequestPostDTO;
import com.harpsharp.infra_rds.dto.board.ResponseCommentDTO;
import com.harpsharp.infra_rds.dto.board.ResponsePostDTO;
import com.harpsharp.infra_rds.dto.user.JoinTestDTO;
import com.harpsharp.infra_rds.dto.user.LoginDTO;
import com.harpsharp.infra_rds.entity.Post;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@ActiveProfiles("local")
@ComponentScan(basePackages = {"com.harpsharp.board", "com.harpsharp.infra_rds"})
@RequiredArgsConstructor
class BoardApplicationTests {

	private final MockMvc mockMvc;
	private final ObjectMapper objectMapper;
	private final RefreshTokenService refreshTokenService;
	private final UserService userService;

	private final String username = "admin";
	private final String password = "HeisPassWord!15";
	private final String email    = "admin@gmail.com";
	private String accessToken    = "EMPTY";
	private Cookie refreshToken   = null;

	private final String title   = "test!";
	private final String content = "blahblah!";

    private final PostService postService;
    private final CommentService commentService;

	Map<Long, ResponsePostDTO> 	  post = null;
	Map<Long, ResponseCommentDTO> comment = null;

	public void init() throws Exception{
		JoinTestDTO joinDTO = JoinTestDTO.builder()
				.username(username)
				.password(password)
				.email(email)
				.build();

		String joinJson = objectMapper.writeValueAsString(joinDTO);


		this.mockMvc.perform(
						post("/join")
								.contentType(MediaType.APPLICATION_JSON)
								.content(joinJson))
				.andReturn();
	}
	public void login() throws Exception {
		init();

		LoginDTO loginDto = LoginDTO.builder()
				.username(username)
				.password(password)
				.build();

		String loginJson = objectMapper.writeValueAsString(loginDto);

		MvcResult result = this.mockMvc.perform(
						post("/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(loginJson))
				.andExpect(status().isCreated())
				.andReturn();

		accessToken  = result.getResponse().getHeader("Authorization").split(" ")[1];
		refreshToken = result.getResponse().getCookie("refresh");
	}

	public void writePost() throws Exception{
		RequestPostDTO requestPostDTO = new RequestPostDTO(username, title, content);
		String postJson = objectMapper.writeValueAsString(requestPostDTO);

		MvcResult result = this.mockMvc.perform(
				post("/board/posts")
						.contentType(MediaType.APPLICATION_JSON)
						.content(postJson))
				.andExpect(status().isCreated())
				.andReturn();


		post = postService.getPostById(1L);


		RequestCommentDTO requestCommentDTO = new RequestCommentDTO(1L, username, content);
		String commentJson = objectMapper.writeValueAsString(requestCommentDTO);

//		MvcResult result = this.mockMvc
//				.perform(
//						post("/board/posts/1/comments")
//								.contentType(MediaType.APPLICATION_JSON)
//								.content(commentJson))
//				.andExpect(status().isCreated())
//				.andReturn();


	}

	@BeforeEach
	public void setUp() throws Exception {
		userService.clear();
		refreshTokenService.clear();
		login();
	}

	@AfterEach
	public void tearDown() throws Exception {
		userService.clear();
		postService.clear();
		commentService.clear();
	}

	@DisplayName("게시글 작성 테스트")
	@Test
	@Transactional
	public void writeTest() throws Exception {
		RequestPostDTO postDTO = new RequestPostDTO(username, title, content);

		this.mockMvc.perform(
						post("/join")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(postDTO)))
				.andExpect(status().isCreated())
				.andDo(document("Join", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("username").description("닉네임"),
								fieldWithPath("title").description("제목"),
								fieldWithPath("content").description("글 내용")
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
										.description("글 작성 성공 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지"),
								fieldWithPath("data")
										.type(JsonFieldType.VARIES)
										.description("등록된 게시글 정보")
						)
				));

	}

	@DisplayName("전체 게시글 조회 테스트")
	@Test
	@Transactional
	public void getAllPostsTest() throws Exception {
		RequestPostDTO postDTO = new RequestPostDTO(username, title, content);

		this.mockMvc.perform(
						get("/board/posts")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(postDTO)))
				.andExpect(status().isCreated())
				.andDo(document("Join", // 문서화할 때 사용할 경로와 이름
						responseFields(
								fieldWithPath("timeStamp")
										.type(JsonFieldType.STRING)
										.description("응답 시간"),
								fieldWithPath("code")
										.type(JsonFieldType.VARIES)
										.description("상태 코드"),
								fieldWithPath("message")
										.type(JsonFieldType.STRING)
										.description("글 작성 성공 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지"),
								fieldWithPath("data")
										.type(JsonFieldType.VARIES)
										.description("전체 게시글 정보")
						)
				));
	}






	// GET board/posts
	// POST board/posts
	// GET board/posts/{postId}
	// PATCH board/posts/{postId}
	// DELETE board/posts/{postId}

	// GET    board/posts/{postId}/comments
	// POST   board/posts/{postId}/comments
	// GET 	  board/posts/{postId}/comments/{commentId}
	// PATCH  board/posts/{postId}/comments/{commentId}
	// DELETE board/posts/{postId}/comments/{commentId}

}
