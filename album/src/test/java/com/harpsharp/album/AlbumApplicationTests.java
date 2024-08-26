package com.harpsharp.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harpsharp.infra_rds.dto.image.RequestProfileImageDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@ComponentScan(basePackages = {"com.harpsharp.album", "com.harpsharp.infra_rds"})
class AlbumApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("PresignedURL 생성")
    @Test
    @Transactional
    public void getPresignedURL() throws Exception {
        String filename = "test.png";
        this.mockMvc.perform(get("/api/v1/profile/presigned/{filename}", filename))
                .andExpect(status().isOk())
                .andDo(document("presigned", // 문서화할 때 사용할 경로와 이름
                        pathParameters(parameterWithName("filename").description("업로드 할 파일명")),
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
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("presignedURL과 S3에 저장될 파일명(UUID)"),
                                fieldWithPath("data.presignedUrl").type(JsonFieldType.STRING).description("presignedURL"),
                                fieldWithPath("data.uuid").type(JsonFieldType.STRING).description("UUID")
                        )));

    }

    @DisplayName("업로드 확인 및 ProfileImage 객체 정보 반환")
    @Test
    @Transactional
    public void postConfirm() throws Exception {
        RequestProfileImageDTO requestProfileImageDTO = new RequestProfileImageDTO("testUUID.png");
        String profileJson = objectMapper.writeValueAsString(requestProfileImageDTO);
        this.mockMvc.perform(post("/api/v1/profile/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content(profileJson))
                .andExpect(status().isOk())
                .andDo(document("Confirm",
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
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("presignedURL과 S3에 저장될 파일명(UUID)"),
                                fieldWithPath("data.url").type(JsonFieldType.STRING).description("실제 정적 URL"),
                                fieldWithPath("data.uuid").type(JsonFieldType.STRING).description("UUID"),
                                fieldWithPath("data.type").type(JsonFieldType.STRING).description("type"),
                                fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("생성 시간"),
                                fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).description("수정 시간")
                        )));
    }
}
