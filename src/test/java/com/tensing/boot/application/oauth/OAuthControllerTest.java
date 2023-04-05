package com.tensing.boot.application.oauth;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensing.boot.application.member.model.dto.MemberDto;
import com.tensing.boot.application.member.service.MemberService;
import com.tensing.boot.common.AcceptanceTestExecutionListener;
import com.tensing.boot.common.ConstrainedFields;
import com.tensing.boot.global.filters.security.model.dto.SecurityDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class OAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    private MemberDto.MemberRequest memberRequest;

    private final String restDocTag = "oauth api";

    @BeforeEach
    void init() {
        log.info("startup");
        //list = new ArrayList<>(Arrays.asList("test1", "test2"));
        memberRequest = MemberDto.MemberRequest.builder()
                .username("test1234")
                .email("test1234@test.test")
                .password("test1234@T")
                .build();
        memberService.createMember(memberRequest);
    }


    @Test
    @DisplayName("토큰 발급")
    void tokensTest() throws Exception {

        // when
        final var tokenRequest = SecurityDto.TokenRequest.builder()
                .grantType(SecurityDto.GranType.ISSUE)
                .username(memberRequest.getUsername())
                .password(memberRequest.getPassword())
                .refreshToken("")
                .build();

        var perform = mockMvc.perform(post("/api/oauth/tokens")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(tokenRequest)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath(SecurityDto.TokenResponse.Fields.accessToken).exists())
                .andExpect(jsonPath(SecurityDto.TokenResponse.Fields.refreshToken).exists());

        // docs
        var fields = new ConstrainedFields(SecurityDto.TokenRequest.class);
        perform.andDo(document("{class-name}/{method-name}",
                resource(ResourceSnippetParameters.builder()
                        .tag(restDocTag)
                        .description("토큰 발급 API")
                        .requestFields(
                                fields.withPath(SecurityDto.TokenRequest.Fields.grantType).description("토큰 발급 방식"),
                                fields.withPath(SecurityDto.TokenRequest.Fields.username).description("login id"),
                                fields.withPath(SecurityDto.TokenRequest.Fields.password).description("login password"),
                                fields.withPath(SecurityDto.TokenRequest.Fields.refreshToken).description("refreshToken")
                        )
                        .responseFields(
                                fieldWithPath(SecurityDto.TokenResponse.Fields.accessToken).description("accessToken"),
                                fieldWithPath(SecurityDto.TokenResponse.Fields.refreshToken).description("refreshToken")
                        )
                        .build())
        ));
    }

}
