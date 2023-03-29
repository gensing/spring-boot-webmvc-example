package com.tensing.boot.application.oauth;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensing.boot.application.member.model.dto.MemberDto;
import com.tensing.boot.application.member.service.MemberService;
import com.tensing.boot.config.SecurityConfiguration;
import com.tensing.boot.global.filters.security.model.dto.SecurityDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Import(SecurityConfiguration.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest // 시큐리티 and 서블릿 filter 는 자동 빈 등록을 안해준다.
public class OAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    private final MemberDto.MemberRequest memberRequest = MemberDto.MemberRequest.builder()
            .username("test1234")
            .email("test1234@test.test")
            .password("test1234@T")
            .build();

    @BeforeEach
    public void setup() {
    }

    @Test
    @Order(0)
    @DisplayName("토큰 발급")
    void tokensTest() throws Exception {

        // given
        memberService.createMember(memberRequest);

        // when
        final var tokenRequest = SecurityDto.TokenRequest.builder()
                .grantType(SecurityDto.GranType.USER_INFO)
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
        perform.andDo(document("{class-name}/{method-name}",
                resource(ResourceSnippetParameters.builder()
                        .tag("토큰 발급 API")
                        .description("토큰 발급 API")
                        .requestFields(
                                fieldWithPath(SecurityDto.TokenRequest.Fields.grantType).description("토큰 발급 방식"),
                                fieldWithPath(SecurityDto.TokenRequest.Fields.username).description("login id"),
                                fieldWithPath(SecurityDto.TokenRequest.Fields.password).description("login password"),
                                fieldWithPath(SecurityDto.TokenRequest.Fields.refreshToken).description("refreshToken")
                        )
                        .responseFields(
                                fieldWithPath(SecurityDto.TokenResponse.Fields.accessToken).description("accessToken"),
                                fieldWithPath(SecurityDto.TokenResponse.Fields.refreshToken).description("refreshToken")
                        )
                        .build())
        ));
    }

}
