package com.harpsharp.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.infra_rds.dto.user.*;
import com.harpsharp.auth.service.RefreshTokenService;
import com.harpsharp.auth.service.UserService;
import com.harpsharp.infra_rds.entity.album.ProfileImage;
import com.harpsharp.infra_rds.repository.ProfileImageJpaRepository;
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

import java.util.UUID;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
	@Autowired
	private ProfileImageJpaRepository profileImageJpaRepository;

	private final String username = "admin";
	private final String password = "HeisPassWord!15";
	private final String email    = "admin@gmail.com";
	private final String url 	  = "https://d2165tdwy08x2f.cloudfront.net/profile/7a185c8f-611d-49df-b829-7e8e66943385.jpg";
	private String accessToken    = "EMPTY";
	private Cookie refreshToken   = null;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	public void setUp() {
		userService.clear();
		refreshTokenService.clear();
		profileImageJpaRepository.deleteAll();
	}

	public void init() throws Exception{
		JoinDTO joinDTO = new JoinDTO(username, password, email, url);

		String joinJson = objectMapper.writeValueAsString(joinDTO);
		ProfileImage profileImage = ProfileImage
				.builder()
				.url(url)
				.uuid(UUID.randomUUID().toString())
				.build();

		profileImageJpaRepository.save(profileImage);

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
				.andExpect(status().isCreated())
				.andReturn();

		accessToken  = result.getResponse().getHeader("Authorization").split(" ")[1];
		refreshToken = result.getResponse().getCookie("refresh");
	}

	@DisplayName("회원가입 테스트")
	@Test
	@Transactional
	public void joinTest() throws Exception{
		JoinDTO user = new JoinDTO(username, password, email, url);

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
								fieldWithPath("email").description("이메일 주소"),
								fieldWithPath("url").description("프로필 이미지 URL")
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
				.andExpect(status().isCreated())
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
								fieldWithPath("data.*.url").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
								fieldWithPath("data.*.createdAt").type(JsonFieldType.STRING).description("가입 날짜"),
								fieldWithPath("data.*.updatedAt").type(JsonFieldType.STRING).description("수정 날짜"),
								fieldWithPath("data.*.socialType").type(JsonFieldType.STRING).description("소셜 계정 가입 타입"),
								fieldWithPath("data.*.role").type(JsonFieldType.STRING).description("권한"),
								fieldWithPath("data.*.posts").type(JsonFieldType.OBJECT).description("작성글"),
								fieldWithPath("data.*.comments").type(JsonFieldType.OBJECT).description("작성 댓글"),
								fieldWithPath("data.*.todoPosts").type(JsonFieldType.OBJECT).description("작성글(TODO)"),
								fieldWithPath("data.*.todoComments").type(JsonFieldType.OBJECT).description("작성 댓글(TODO)")),
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
						get("/api/v1/user/{username}", username)
								.contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
								.content(infoJson))
				.andExpect(status().isOk())
				.andDo(document("Get User Data", // 문서화할 때 사용할 경로와 이름
						pathParameters(parameterWithName("username").description("유저명")),
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
								fieldWithPath("data.*.url").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
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
						get("/api/v1/user/board/posts/{username}", username)
								.contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
								.content(infoJson))
				.andExpect(status().isOk())
				.andDo(document("Get User's Posts", // 문서화할 때 사용할 경로와 이름
						pathParameters(parameterWithName("username").description("유저명")),
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
						get("/api/v1/user/board/comments/{username}", username)
								.contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
								.content(infoJson))
				.andExpect(status().isOk())
				.andDo(document("Get User's Comments", // 문서화할 때 사용할 경로와 이름
						pathParameters(parameterWithName("username").description("유저명")),
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
						get("/api/v1/user/todo/posts/{username}", username)
								.contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
								.content(infoJson))
				.andExpect(status().isOk())
				.andDo(document("Get User's Todo Posts", // 문서화할 때 사용할 경로와 이름
						pathParameters(parameterWithName("username").description("유저명")),
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
						get("/api/v1/user/todo/comments/{username}", username)
								.contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
								.content(infoJson))
				.andExpect(status().isOk())
				.andDo(document("Get Users Todo Comments", // 문서화할 때 사용할 경로와 이름
						pathParameters(parameterWithName("username").description("유저명")),
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
				new UpdateUserDTO(password,"update!", "UpdatePassword15!", "update@gmail.com", null);

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
								fieldWithPath("updatedEmail").description("새 이메일 (if null: 변경 X)"),
								fieldWithPath("updatedURL").description("새 프로필 이미지 URL")
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
								fieldWithPath("data.*.url").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
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

	@DisplayName("토큰 재발급 테스트")
	@Test
	@Transactional
	public void reissue() throws Exception{
		String prevAccess = accessToken;
		login();

		this.mockMvc.perform(
				get("/api/v1/reissue")
						.header("Authorization", "Bearer " + prevAccess)
						.cookie(refreshToken))
						.andExpect(status().isOk())
				.andDo(document("Reissue",
						requestHeaders(headerWithName("Authorization").description("유효한 access token")),
						requestCookies(cookieWithName("refresh").description("유효한 refresh token")),
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
								)));
	}

	@DisplayName("Profile Image URL 테스트")
	@Test
	@Transactional
	public void getProfileImageURL() throws Exception{
		login();

		this.mockMvc.perform(
						get("/api/v1/user/profile/{username}", username))
				.andExpect(status().isOk())
				.andDo(document("GetProfileImageURL",
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
										.description("상세 메세지"),
								fieldWithPath("data").type(JsonFieldType.OBJECT).description("프로필 이미지 URL 정보"),
								fieldWithPath("data.url").type(JsonFieldType.STRING).description("프로필 이미지 URL")
						)));
	}
}
