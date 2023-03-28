package com.tensing.boot.application.member;


import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensing.boot.application.member.model.dto.MemberDto;
import com.tensing.boot.config.security.WithMockCustomUser;
import com.tensing.boot.global.filters.security.model.code.RoleCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Slf4j
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final MemberDto.MemberRequest memberRequest = MemberDto.MemberRequest.builder().username("test1234").email("test1234@test.test").password("test1234@T").build();

    @Test
    @Order(0)
    @DisplayName("유저 생성")
    void postTest() throws Exception {

        // given
        final var body = objectMapper.writeValueAsString(memberRequest);

        log.info("******** START : MOC MVC test **********");

        // when
        var perform = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(body));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/members/1"));

        // docs
        perform.andDo(document("{class-name}/{method-name}",
                resource(ResourceSnippetParameters.builder()
                        .tag("유저 api")
                        .description("유저 생성 API")
                        .requestFields(
                                fieldWithPath(MemberDto.MemberRequest.Fields.username).description("The username of a new member"),
                                fieldWithPath(MemberDto.MemberRequest.Fields.email).description("The email of a new member"),
                                fieldWithPath(MemberDto.MemberRequest.Fields.password).description("The password of a new member")
                        )
                        .responseHeaders(
                                headerWithName("Location").description("review detail resource id")
                        )
                        .build())
        ));
        log.info("******** END : MOC MVC test **********");
    }

    @Test
    @Order(1)
    @WithMockCustomUser(id = 1, role = RoleCode.USER)
    @DisplayName("유저 정보 확인")
    void getTest() throws Exception {

        log.info("******** START : MOC MVC test **********");

        // when
        var perform = mockMvc.perform(get("/api/members/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, " ")
                .characterEncoding(StandardCharsets.UTF_8));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath(MemberDto.MemberRequest.Fields.username).value(memberRequest.getUsername()))
                .andExpect(jsonPath(MemberDto.MemberRequest.Fields.email).value(memberRequest.getEmail()));

        // docs
        perform.andDo(document("{class-name}/{method-name}",

                resource(ResourceSnippetParameters.builder()
                        .tag("유저 api")
                        .description("유저 조회 API")
                        .requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                        )
                        .pathParameters(
                                parameterWithName("id").description("The id of the member"))
                        .responseFields(
                                fieldWithPath(MemberDto.MemberRequest.Fields.username).description("The username of the member"),
                                fieldWithPath(MemberDto.MemberRequest.Fields.email).description("The email of the member")
                        )
                        .build())
        ));
        log.info("******** END : MOC MVC test **********");
    }
}
