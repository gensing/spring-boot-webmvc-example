package com.tensing.boot.application.member;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensing.boot.application.member.model.dto.MemberDto;
import com.tensing.boot.application.member.service.MemberService;
import com.tensing.boot.config.security.WithMockCustomUser;
import com.tensing.boot.global.filters.security.model.code.RoleCode;
import com.tensing.boot.global.filters.security.model.dto.SecurityDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@Slf4j
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
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

        final var body = objectMapper.writeValueAsString(memberRequest);

        log.info("******** START : MOC MVC test **********");
        mockMvc.perform(post("/api/members").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/members/1"))
                .andDo(print())
        // rest-docs 문서 생성
//                .andDo(document("{class-name}/{method-name}",
//                        requestFields(
//                                fieldWithPath(MemberDto.MemberRequest.Fields.username).description("The username of a new member"),
//                                fieldWithPath(MemberDto.MemberRequest.Fields.email).description("The email of a new member"),
//                                fieldWithPath(MemberDto.MemberRequest.Fields.password).description("The password of a new member")
//                        ),
//                        responseHeaders(
//                                headerWithName("Location").description("review detail resource id")
//                        )
//                ))
        ;
        log.info("******** END : MOC MVC test **********");
    }

    @Test
    @Order(1)
    @WithMockCustomUser(id = 1, role = RoleCode.USER)
    @DisplayName("유저 정보 확인")
    void getTest() throws Exception {

        log.info("******** START : MOC MVC test **********");
        mockMvc.perform(get("/api/members/{id}", 1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(MemberDto.MemberRequest.Fields.username).value(memberRequest.getUsername()))
                .andExpect(jsonPath(MemberDto.MemberRequest.Fields.email).value(memberRequest.getEmail()))
                .andDo(print())
        // rest-docs 문서 생성
//                .andDo(document("{class-name}/{method-name}",
//                        requestHeaders(
//                                headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
//                        ),
//                        pathParameters(
//                                parameterWithName("id").description("The id of the member")),
//                        responseFields(
//                                fieldWithPath(MemberDto.MemberRequest.Fields.username).description("The username of the member"),
//                                fieldWithPath(MemberDto.MemberRequest.Fields.email).description("The email of the member")
//                        )
//                ))
        ;
        log.info("******** END : MOC MVC test **********");
    }
}
