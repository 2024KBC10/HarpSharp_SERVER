package com.harpsharp.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.infra_rds.dto.user.DeleteDTO;
import com.harpsharp.infra_rds.dto.user.JoinTestDTO;
import com.harpsharp.infra_rds.dto.user.LoginDTO;
import com.harpsharp.infra_rds.dto.user.UpdateUserDTO;
import com.harpsharp.auth.service.RefreshTokenService;
import com.harpsharp.auth.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
class AuthApplicationTests {
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
    @Autowired
    private PasswordEncoder passwordEncoder;

	@BeforeEach
	public void setUp() {
		userService.clear();
		refreshTokenService.clear();
	}

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

	@DisplayName("회원가입 테스트")
	@Test
	@Transactional
	public void joinTest() throws Exception{
		JoinTestDTO user = JoinTestDTO.builder()
				.username(username)
				.password(password)
				.email(email)
				.build();

		String json = objectMapper.writeValueAsString(user);

		
		this.mockMvc.perform(
				post("/join")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isCreated())
				.andDo(document("Join", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("username").description("닉네임"),
								fieldWithPath("password").description("비밀번호"),
								fieldWithPath("email").description("이메일 주소")
						),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING)
										.description("회원가입 성공 여부"),
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("상세 메시지")
						)
				));
	}


	@DisplayName("로그인 테스트")
	@Test
	@Transactional
	public void loginTest() throws Exception{
		init();

		LoginDTO loginDto = LoginDTO.builder()
				.username(username)
				.password(password)
				.build();

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
								fieldWithPath("code").type(JsonFieldType.STRING)
										.description("로그인 상태"),
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("상세 메시지")
						),
						responseHeaders(headerWithName("Authorization").description("발급된 access token"))
				));
	}

	@DisplayName("유저 정보 업데이트 테스트")
	@Test
	@Transactional
	public void updateTest() throws Exception{
		login();

		UpdateUserDTO updateUserDto = UpdateUserDTO
				.builder()
				.username("update!")
				.password("updatePassWord!5")
				.email("update@gmail.com")
				.build();

		String updateJson = objectMapper.writeValueAsString(updateUserDto);

		this.mockMvc.perform(
						patch("/user")
								.header("Authorization", "Bearer " + accessToken)
								.cookie(refreshToken)
								.contentType(MediaType.APPLICATION_JSON)
								.content(updateJson))
				.andExpect(status().is3xxRedirection())
				.andDo(document("Update", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("username").description("새 닉네임 (if null: 변경 X)"),
								fieldWithPath("password").description("새 비밀번호 (if null: 변경 X)"),
								fieldWithPath("email").description("새 이메일 (if null: 변경 X)")
						),
						requestHeaders(headerWithName("Authorization").description("유효한 access token")),
						requestCookies(cookieWithName("refresh").description("유효한 refresh token")),
						responseHeaders(headerWithName("Authorization").description("재발급된 access token"))
				));
	}

	@DisplayName("회원탈퇴 테스트")
	@Test
	@Transactional
	public void deleteTest() throws Exception{
		login();

		DeleteDTO deleteDTO = DeleteDTO
				.builder()
				.password(password)
				.build();

		String deleteJson = objectMapper.writeValueAsString(deleteDTO);

		this.mockMvc.perform(
				delete("/user")
						.header("Authorization", "Bearer " + accessToken)
						.cookie(refreshToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(deleteJson))
				.andExpect(status().isOk())
				.andDo(document("Delete", // 문서화할 때 사용할 경로와 이름
						requestHeaders(headerWithName("Authorization").description("유효한 access token")),
						requestCookies(cookieWithName("refresh").description("유효한 refresh token")),
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("password").description("입력한 비밀번호")
						),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING)
										.description("회원 탈퇴 성공 여부"),
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("상세 메시지")
						)
				));
	}
}
