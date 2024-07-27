package com.harpsharp.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.auth.dto.JoinTestDTO;
import com.harpsharp.auth.dto.LoginDTO;
import com.harpsharp.auth.dto.UpdateDTO;
import com.harpsharp.auth.jwt.JwtUtil;
import com.harpsharp.auth.repository.UserRepository;
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

import org.springframework.http.MediaType;

import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.cookies.CookieDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@RequiredArgsConstructor
class AuthApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtUtil jwtUtil;

	private final String username = "admin";
	private final String password = "heisadmin45!";
	private final String email    = "admin@gmail.com";
	private String accessToken    = "EMPTY";
	private Cookie refreshToken   = null;

	@BeforeEach
	public void setUp() {
		userRepository.deleteAll();
		jwtUtil.clearRefreshRepository();
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
		System.out.println("accessToken = " + accessToken);
		System.out.println("refreshToken = " + refreshToken);
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

		UpdateDTO updateDto = UpdateDTO
				.builder()
				.username("update!")
				.password("update11!")
				.email("update@gmail.com")
				.build();

		String updateJson = objectMapper.writeValueAsString(updateDto);

		this.mockMvc.perform(
						patch("/user/update")
								.header("Authorization", "Bearer " + accessToken)
								.cookie(refreshToken)
								.contentType(MediaType.APPLICATION_JSON)
								.content(updateJson))
				.andExpect(status().isOk())
				.andDo(document("Update", // 문서화할 때 사용할 경로와 이름
						requestFields( // 요청 파라미터 문서화
								fieldWithPath("username").description("새 닉네임 (if null: 변경 X)"),
								fieldWithPath("password").description("새 비밀번호 (if null: 변경 X)"),
								fieldWithPath("email").description("새 이메일 (if null: 변경 X)")
						),
						requestHeaders(headerWithName("Authorization").description("유효한 access token")),
						requestCookies(cookieWithName("refresh").description("유효한 refresh token")),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING)
										.description("업데이트 상태"),
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("상세 메시지")
						),
						responseHeaders(headerWithName("Authorization").description("재발급된 access token"))
				));
	}

	@DisplayName("회원탈퇴 테스트")
	@Test
	@Transactional
	public void deleteTest() throws Exception{
		login();
		this.mockMvc.perform(
				delete("/user/delete")
						.header("Authorization", "Bearer " + accessToken)
						.cookie(refreshToken)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(document("Delete", // 문서화할 때 사용할 경로와 이름
						requestHeaders(headerWithName("Authorization").description("유효한 access token")),
						requestCookies(cookieWithName("refresh").description("유효한 refresh token")),
						responseFields(
								fieldWithPath("code").type(JsonFieldType.STRING)
										.description("회원 탈퇴 성공 여부"),
								fieldWithPath("message").type(JsonFieldType.STRING)
										.description("상세 메시지")
						)
				));
	}
}
