package com.tensing.boot.application.member;


import com.epages.restdocs.apispec.ResourceSnippetParameters;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensing.boot.application.member.model.dto.MemberDto;
import com.tensing.boot.application.member.service.MemberService;
import com.tensing.boot.common.AcceptanceTestExecutionListener;
import com.tensing.boot.common.ConstrainedFields;
import com.tensing.boot.global.filters.security.Const;
import com.tensing.boot.global.filters.security.model.dto.SecurityDto;
import com.tensing.boot.global.filters.security.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static com.epages.restdocs.apispec.Schema.schema;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private SecurityService securityService;

    private final String REST_DOC_TAG = "member api";

    private String bearerToken;

    private MemberDto.MemberResponse savedMember;


    @BeforeEach
    void init() {

        log.info("before each");
        var memberRequest = MemberDto.MemberRequest.builder()
                .username("test1234")
                .email("test1234@test.test")
                .password("test1234@T")
                .build();

        savedMember = memberService.createMember(memberRequest);

        var tokenRequest = SecurityDto.TokenRequest.builder()
                .grantType(SecurityDto.GranType.ISSUE)
                .username(memberRequest.getUsername())
                .password(memberRequest.getPassword())
                .build();

        this.bearerToken = Const.BEARER_PREFIX + securityService.getToken(tokenRequest).getAccessToken();

    }

    @Nested
    @DisplayName("유저 생성")
    class MemberGenerateTest {
        @Test
        void generate_success() throws Exception {

            // given
            final var body = objectMapper.writeValueAsString(MemberDto.MemberRequest.builder()
                    .username("test12345")
                    .email("test12345@test.test")
                    .password("test12345@T")
                    .build());

            // when
            var perform = mockMvc.perform(post("/api/members")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(body))
                    .andDo(print());

            // then
            perform.andExpect(status().isCreated());


            // docs
            var fields = new ConstrainedFields(MemberDto.MemberRequest.class);
            perform.andDo(document("{class-name}/{method-name}",
                    resource(ResourceSnippetParameters.builder()
                            .tag(REST_DOC_TAG)
                            .summary("유저 생성 API")
                            .description("유저 생성 API")
                            .requestSchema(schema(MemberDto.MemberRequest.class.getSimpleName()))
                            .requestFields(
                                    fields.withPath(MemberDto.MemberRequest.Fields.username).description("The username of a new member"),
                                    fields.withPath(MemberDto.MemberRequest.Fields.email).description("The email of a new member"),
                                    fields.withPath(MemberDto.MemberRequest.Fields.password).description("The password of a new member")
                            )
                            .responseHeaders(
                                    headerWithName("Location").description("review detail resource id")
                            )
                            .responseSchema(schema(MemberDto.MemberResponse.class.getSimpleName()))
                            .responseFields(
                                    fieldWithPath(MemberDto.MemberResponse.Fields.id).description("The id of the member"),
                                    fieldWithPath(MemberDto.MemberResponse.Fields.username).description("The username of the member"),
                                    fieldWithPath(MemberDto.MemberResponse.Fields.email).description("The email of the member")
                            )
                            .build()
                    )
            ));
        }
    }

    @DisplayName("유저 정보 확인")
    class MemberGetTest {
        @Test
        void get_success() throws Exception {

            // when
            var perform = mockMvc.perform(get("/api/members/{id}", savedMember.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, bearerToken)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andDo(print());

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath(MemberDto.MemberResponse.Fields.username).value(savedMember.getUsername()))
                    .andExpect(jsonPath(MemberDto.MemberResponse.Fields.email).value(savedMember.getEmail()));

            // docs
            perform.andDo(document("{class-name}/{method-name}",
                    resource(ResourceSnippetParameters.builder()
                            .tag(REST_DOC_TAG)
                            .summary("유저 조회 API")
                            .description("유저 조회 API")
                            .requestHeaders(
                                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                            )
                            .pathParameters(
                                    parameterWithName("id").description("The id of the member")
                            )
                            .responseSchema(schema(MemberDto.MemberResponse.class.getSimpleName()))
                            .responseFields(
                                    fieldWithPath(MemberDto.MemberResponse.Fields.id).description("The id of the member"),
                                    fieldWithPath(MemberDto.MemberResponse.Fields.username).description("The username of the member"),
                                    fieldWithPath(MemberDto.MemberResponse.Fields.email).description("The email of the member")
                            )
                            .build()
                    )
            ));
        }
    }


}
