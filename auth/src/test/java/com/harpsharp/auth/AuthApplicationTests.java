package com.harpsharp.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.infra_rds.dto.user.*;
import com.harpsharp.auth.service.RefreshTokenService;
import com.harpsharp.auth.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@ComponentScan(basePackages = {"com.harpsharp.auth", "com.harpsharp.infra_rds"})
@ActiveProfiles("local")
class AuthApplicationTests {
	@Autowired
	private  MockMvc mockMvc;
	@Autowired
	private  ObjectMapper objectMapper;
	@Autowired
	private  RefreshTokenService refreshTokenService;
	@Autowired
	private  UserService userService;


	private final String username = "admin";
	private final String password = "HeisPassWord!15";
	private final String email    = "admin@gmail.com";
	private String accessToken    = "EMPTY";
	private Cookie refreshToken   = null;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	public void setUp() {
		userService.clear();
		refreshTokenService.clear();
	}

	public void init() throws Exception{
		JoinTestDTO joinDTO = new JoinTestDTO(username, password, email);

		String joinJson = objectMapper.writeValueAsString(joinDTO);


		this.mockMvc.perform(
				post("/api/v1/join")
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

	@DisplayName("Auth 서버 루트 페이지")
	@Test
	@Transactional
	public void rootPage() throws Exception {
		this.mockMvc.perform(get("/api/v1/"))
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
										.description("접속 성공 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지")
						)));
	}

	@DisplayName("회원가입 테스트")
	@Test
	@Transactional
	public void joinTest() throws Exception{
		JoinTestDTO user = new JoinTestDTO(username, password, email);

		String json = objectMapper.writeValueAsString(user);


		this.mockMvc.perform(
				post("/api/v1/join")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk())
				.andDo(document("Join", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("username").description("닉네임"),
								fieldWithPath("password").description("비밀번호"),
								fieldWithPath("email").description("이메일 주소")
						),
						responseFields(
								fieldWithPath("timeStamp")
										.type(JsonFieldType.STRING)
										.description("응답 시간"),
								fieldWithPath("code")
										.type(JsonFieldType.VARIES)
										.description("상태 코드"),
								fieldWithPath("message")
										.type(JsonFieldType.STRING)
										.description("회원가입 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지")
						)
				));
	}


	@DisplayName("로그인 테스트")
	@Test
	@Transactional
	public void loginTest() throws Exception{
		init();

		LoginDTO loginDto = new LoginDTO(username, password);

		String loginJson = objectMapper.writeValueAsString(loginDto);

		this.mockMvc.perform(
						post("/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(loginJson))
				.andExpect(status().isOk())
				.andDo(document("Login", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("username").description("닉네임"),
								fieldWithPath("password").description("비밀번호")
						),
						responseFields(
								fieldWithPath("timeStamp")
										.type(JsonFieldType.STRING)
										.description("응답 시간"),
								fieldWithPath("code")
										.type(JsonFieldType.VARIES)
										.description("상태 코드"),
								fieldWithPath("message")
										.type(JsonFieldType.STRING)
										.description("로그인 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지"),
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("작성자 정보"),
								fieldWithPath("data.*.username").type(JsonFieldType.STRING).description("닉네임"),
								fieldWithPath("data.*.email").type(JsonFieldType.STRING).description("이메일"),
								fieldWithPath("data.*.createdAt").type(JsonFieldType.STRING).description("가입 날짜"),
								fieldWithPath("data.*.updatedAt").type(JsonFieldType.STRING).description("수정 날짜"),
								fieldWithPath("data.*.socialType").type(JsonFieldType.STRING).description("소셜 계정 가입 타입"),
								fieldWithPath("data.*.role").type(JsonFieldType.STRING).description("권한"),
								fieldWithPath("data.*.posts").type(JsonFieldType.OBJECT).description("작성글"),
								fieldWithPath("data.*.comments").type(JsonFieldType.OBJECT).description("작성 댓글"),
								fieldWithPath("data.*.todoPosts").type(JsonFieldType.OBJECT).description("작성글(TODO)"),
								fieldWithPath("data.*.todoComments").type(JsonFieldType.OBJECT).description("작성 댓글(TODO)")

						),
						responseHeaders(headerWithName("Authorization").description("발급된 access token"))
				));
	}


	@DisplayName("로그아웃 테스트")
	@Test
	@Transactional
	public void logoutTest() throws Exception{
		login();

		this.mockMvc.perform(
						post("/logout")
								.header("Authorization", "Bearer " + accessToken)
								.cookie(refreshToken))
				.andExpect(status().isOk())
				.andDo(document("Logout", // 문서화할 때 사용할 경로와 이름
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
										.description("로그아웃 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지")
						)
				));
	}

	@DisplayName("유저 정보 조회 테스트")
	@Test
	@Transactional
	public void getUserDataTest() throws Exception{
		login();
		InfoDTO infoDto = new InfoDTO(username, "ROLE_USER");

		String infoJson = objectMapper.writeValueAsString(infoDto);


		this.mockMvc.perform(
						get("/api/v1/user")
								.contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
								.content(infoJson))
				.andExpect(status().isOk())
				.andDo(document("Get User Data", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("username").description("닉네임"),
								fieldWithPath("role").description("권한")
						),
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
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("작성자 정보"),
								fieldWithPath("data.*.username").type(JsonFieldType.STRING).description("닉네임"),
								fieldWithPath("data.*.email").type(JsonFieldType.STRING).description("이메일"),
								fieldWithPath("data.*.createdAt").type(JsonFieldType.STRING).description("가입 날짜"),
								fieldWithPath("data.*.updatedAt").type(JsonFieldType.STRING).description("수정 날짜"),
								fieldWithPath("data.*.socialType").type(JsonFieldType.STRING).description("소셜 계정 가입 타입"),
								fieldWithPath("data.*.role").type(JsonFieldType.STRING).description("권한"),
								fieldWithPath("data.*.posts").type(JsonFieldType.OBJECT).description("작성글"),
								fieldWithPath("data.*.comments").type(JsonFieldType.OBJECT).description("작성 댓글"),
								fieldWithPath("data.*.todoPosts").type(JsonFieldType.OBJECT).description("작성글(TODO)"),
								fieldWithPath("data.*.todoComments").type(JsonFieldType.OBJECT).description("작성 댓글(TODO)"))
				));
	}

	@DisplayName("작성글 조회 테스트")
	@Test
	@Transactional
	public void getPostsByUserInfo() throws Exception{
		login();
		InfoDTO infoDto = new InfoDTO(username, password);

		String infoJson = objectMapper.writeValueAsString(infoDto);


		this.mockMvc.perform(
						get("/api/v1/user/board/posts")
								.contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
								.content(infoJson))
				.andExpect(status().isOk())
				.andDo(document("Get User's Posts", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("username").description("닉네임"),
								fieldWithPath("role").description("권한")
						),
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
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("작성글 정보(All)"))
				));
	}


	@DisplayName("작성 댓글 조회 테스트")
	@Test
	@Transactional
	public void getCommentsByUserInfo() throws Exception{
		login();
		InfoDTO infoDto = new InfoDTO(username, password);

		String infoJson = objectMapper.writeValueAsString(infoDto);


		this.mockMvc.perform(
						get("/api/v1/user/board/comments")
								.contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
								.content(infoJson))
				.andExpect(status().isOk())
				.andDo(document("Get User's Comments", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("username").description("닉네임"),
								fieldWithPath("role").description("권한")
						),
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
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("작성 댓글 정보(All)"))
				));
	}


	@DisplayName("작성글(TODO) 조회 테스트")
	@Test
	@Transactional
	public void getTodoPostsByUserInfo() throws Exception{
		login();
		InfoDTO infoDto = new InfoDTO(username, password);

		String infoJson = objectMapper.writeValueAsString(infoDto);


		this.mockMvc.perform(
						get("/api/v1/user/todo/posts")
								.contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
								.content(infoJson))
				.andExpect(status().isOk())
				.andDo(document("Get User's Todo Posts", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("username").description("닉네임"),
								fieldWithPath("role").description("권한")
						),
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
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("작성 TODO 게시글 정보(All)"))
				));
	}


	@DisplayName("작성 댓글(TODO) 조회 테스트")
	@Test
	@Transactional
	public void getTodoCommentsByUserInfo() throws Exception{
		login();
		InfoDTO infoDto = new InfoDTO(username, password);

		String infoJson = objectMapper.writeValueAsString(infoDto);


		this.mockMvc.perform(
						get("/api/v1/user/todo/comments")
								.contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
								.content(infoJson))
				.andExpect(status().isOk())
				.andDo(document("Get Users Todo Comments", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("username").description("닉네임"),
								fieldWithPath("role").description("권한")
						),
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
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("작성 TODO 댓글 정보(All)"))
				));
	}



	@DisplayName("유저 정보 업데이트 테스트")
	@Test
	@Transactional
	public void updateTest() throws Exception{
		login();

		UpdateUserDTO updateUserDto =
				new UpdateUserDTO(password,"update!", "UpdatePassword15!", "update@gmail.com");

		String updateJson = objectMapper.writeValueAsString(updateUserDto);

		this.mockMvc.perform(
						patch("/api/v1/user")
								.header("Authorization", "Bearer " + accessToken)
								.cookie(refreshToken)
								.contentType(MediaType.APPLICATION_JSON)
								.content(updateJson))
				.andExpect(status().isOk())
				.andDo(document("Update", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("password").description("기존 비밀번호"),
								fieldWithPath("updatedUsername").description("새 닉네임 (if null: 변경 X)"),
								fieldWithPath("updatedPassword").description("새 비밀번호 (if null: 변경 X)"),
								fieldWithPath("updatedEmail").description("새 이메일 (if null: 변경 X)")
						),
						requestHeaders(headerWithName("Authorization").description("유효한 access token")),
						requestCookies(cookieWithName("refresh").description("유효한 refresh token")),
						responseHeaders(headerWithName("Authorization").description("재발급된 access token")),
						responseFields(
								fieldWithPath("timeStamp")
										.type(JsonFieldType.STRING)
										.description("응답 시간"),
								fieldWithPath("code")
										.type(JsonFieldType.VARIES)
										.description("상태 코드"),
								fieldWithPath("message")
										.type(JsonFieldType.STRING)
										.description("업데이트 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지"),
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("작성자 정보"),
								fieldWithPath("data.*.username").type(JsonFieldType.STRING).description("닉네임"),
								fieldWithPath("data.*.email").type(JsonFieldType.STRING).description("이메일"),
								fieldWithPath("data.*.createdAt").type(JsonFieldType.STRING).description("가입 날짜"),
								fieldWithPath("data.*.updatedAt").type(JsonFieldType.STRING).description("수정 날짜"),
								fieldWithPath("data.*.socialType").type(JsonFieldType.STRING).description("소셜 계정 가입 타입"),
								fieldWithPath("data.*.role").type(JsonFieldType.STRING).description("권한"),
								fieldWithPath("data.*.posts").type(JsonFieldType.OBJECT).description("작성글"),
								fieldWithPath("data.*.comments").type(JsonFieldType.OBJECT).description("작성 댓글"),
								fieldWithPath("data.*.todoPosts").type(JsonFieldType.OBJECT).description("작성글(TODO)"),
								fieldWithPath("data.*.todoComments").type(JsonFieldType.OBJECT).description("작성 댓글(TODO)"))
				));
	}

	@DisplayName("회원탈퇴 테스트")
	@Test
	@Transactional
	public void deleteTest() throws Exception{
		login();

		DeleteDTO deleteDTO = new DeleteDTO(password);
		String deleteJson = objectMapper.writeValueAsString(deleteDTO);

		this.mockMvc.perform(
				delete("/api/v1/user")
						.header("Authorization", "Bearer " + accessToken)
						.cookie(refreshToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(deleteJson))
				.andExpect(status().isOk())
				.andDo(document("Delete", // 문서화할 때 사용할 경로와 이름
						requestHeaders(headerWithName("Authorization").description("유효한 access token")),
						requestCookies(cookieWithName("refresh").description("유효한 refresh token")),
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("password").description("유저가 입력한 비밀번호 (본인 확인용)")
						),
						responseFields(
								fieldWithPath("timeStamp")
										.type(JsonFieldType.STRING)
										.description("응답 시간"),
								fieldWithPath("code")
										.type(JsonFieldType.VARIES)
										.description("상태 코드"),
								fieldWithPath("message")
										.type(JsonFieldType.STRING)
										.description("회원 탈퇴 여부"),
								fieldWithPath("details")
										.type(JsonFieldType.STRING)
										.description("상세 메세지")
						)
				));
	}
}
