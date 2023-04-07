package com.tensing.boot.application.post;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensing.boot.application.member.model.dto.MemberDto;
import com.tensing.boot.application.member.service.MemberService;
import com.tensing.boot.application.post.model.dto.PostDto;
import com.tensing.boot.application.post.service.PostService;
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

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.Schema.schema;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PostService postService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private SecurityService securityService;

    private final String REST_DOC_TAG = "post api";

    private String bearerToken;

    private PostDto.PostRequest postRequest;

    private long postId;

    @BeforeEach
    void init() {

        log.info("before each");
        var memberRequest = MemberDto.MemberRequest.builder()
                .username("test1234")
                .email("test1234@test.test")
                .password("test1234@T")
                .build();

        var memberId = memberService.createMember(memberRequest).getId();

        var tokenRequest = SecurityDto.TokenRequest.builder()
                .grantType(SecurityDto.GranType.ISSUE)
                .username(memberRequest.getUsername())
                .password(memberRequest.getPassword())
                .build();

        this.bearerToken = Const.BEARER_PREFIX + securityService.getToken(tokenRequest).getAccessToken();

        this.postRequest = PostDto.PostRequest.builder()
                .title("test title 1")
                .body("test body 1")
                .build();

        this.postId = postService.insert(postRequest, SecurityDto.UserInfo.builder().id(memberId).build());
    }

    @Nested
    @DisplayName("post 조회")
    class PostGetTest {
        @Test
        void get_success() throws Exception {

            // when
            var perform = mockMvc.perform(get("/api/posts/{id}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
            ).andDo(print());

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath(PostDto.PostResponse.Fields.id).value(postId))
                    .andExpect(jsonPath(PostDto.PostResponse.Fields.title).value(postRequest.getTitle()))
                    .andExpect(jsonPath(PostDto.PostResponse.Fields.body).value(postRequest.getBody()));

            // docs
            perform.andDo(document("{class-name}/{method-name}",
                    resource(ResourceSnippetParameters.builder()
                            .tag(REST_DOC_TAG)
                            .summary("post 조회 API")
                            .description("post 조회 API")
                            .pathParameters(
                                    parameterWithName("id").description("The id of the post")
                            )
                            .responseSchema(schema(PostDto.PostResponse.class.getSimpleName()))
                            .responseFields(
                                    fieldWithPath(PostDto.PostResponse.Fields.id).description("The id of the post"),
                                    fieldWithPath(PostDto.PostResponse.Fields.writer).description("The writer of the post"),
                                    fieldWithPath(PostDto.PostResponse.Fields.title).description("The title of the post"),
                                    fieldWithPath(PostDto.PostResponse.Fields.body).description("The body of the post"),
                                    fieldWithPath(PostDto.PostResponse.Fields.createdDate).description("The createdDate of the post"),
                                    fieldWithPath(PostDto.PostResponse.Fields.updatedDate).description("The updatedDate of the post")
                            )
                            .build())
            ));
        }
    }

    @Nested
    @DisplayName("post list 조회")
    class PostListTest {
        @Test
        void getList_success() throws Exception {

            // when
            var perform = mockMvc.perform(get("/api/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
            ).andDo(print());

            // then
            perform.andExpect(status().isOk());

            // docs
            perform.andDo(document("{class-name}/{method-name}",
                    resource(ResourceSnippetParameters.builder()
                            .tag(REST_DOC_TAG)
                            .summary("post list 조회 API")
                            .description("post list 조회 API")
                            .responseSchema(schema(PostDto.PostResponse.class.getSimpleName()))
                            .responseFields(
                                    fieldWithPath("[]." + PostDto.PostResponse.Fields.id).description("The id of the post"),
                                    fieldWithPath("[]." + PostDto.PostResponse.Fields.writer).description("The writer of the post"),
                                    fieldWithPath("[]." + PostDto.PostResponse.Fields.title).description("The title of the post"),
                                    fieldWithPath("[]." + PostDto.PostResponse.Fields.body).description("The body of the post"),
                                    fieldWithPath("[]." + PostDto.PostResponse.Fields.createdDate).description("The createdDate of the post"),
                                    fieldWithPath("[]." + PostDto.PostResponse.Fields.updatedDate).description("The updatedDate of the post")
                            )
                            .build())
            ));
        }
    }

    @Nested
    @DisplayName("post 생성")
    class PostCreateTest {
        @Test
        void create_success() throws Exception {

            // when
            var postRequest = PostDto.PostRequest.builder().title("test title 1").body("test body 1").build();

            var perform = mockMvc.perform(post("/api/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .header(HttpHeaders.AUTHORIZATION, bearerToken)
                    .content(objectMapper.writeValueAsString(postRequest))
            ).andDo(print());

            // then
            perform.andExpect(status().isCreated());

            // docs
            var fields = new ConstrainedFields(PostDto.PostRequest.class);
            perform.andDo(document("{class-name}/{method-name}",
                    resource(ResourceSnippetParameters.builder()
                            .tag(REST_DOC_TAG)
                            .summary("post 생성 API")
                            .description("post 생성 API")
                            .requestHeaders(
                                    headerWithName(HttpHeaders.AUTHORIZATION).description("jwt token")
                            )
                            .requestSchema(schema(PostDto.PostRequest.class.getSimpleName()))
                            .requestFields(
                                    fields.withPath(PostDto.PostRequest.Fields.title).description("The title of a new post"),
                                    fields.withPath(PostDto.PostRequest.Fields.body).description("The body of a new post")
                            )
                            .responseHeaders(
                                    headerWithName("Location").description("review detail resource id")
                            )
                            .build())
            ));
        }
    }

    @Nested
    @DisplayName("post 수정")
    class PostUpdateTest {
        @Test
        void put_success() throws Exception {

            // when
            var postRequest = PostDto.PostPutRequest.builder()
                    .title("test update title 1")
                    .body("test update body 1")
                    .build();

            var perform = mockMvc.perform(put("/api/posts/{id}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .header(HttpHeaders.AUTHORIZATION, bearerToken)
                    .content(objectMapper.writeValueAsString(postRequest))
            ).andDo(print());

            // then
            perform.andExpect(status().isOk());

            // docs
            var fields = new ConstrainedFields(PostDto.PostPutRequest.class);
            perform.andDo(document("{class-name}/{method-name}",
                    resource(ResourceSnippetParameters.builder()
                            .tag(REST_DOC_TAG)
                            .summary("post 수정 API")
                            .description("post 수정 API")
                            .pathParameters(
                                    parameterWithName("id").description("The id of the post")
                            )
                            .requestSchema(schema(PostDto.PostPutRequest.class.getSimpleName()))
                            .requestFields(
                                    fields.withPath(PostDto.PostPutRequest.Fields.title).description("The title of a update post"),
                                    fields.withPath(PostDto.PostPutRequest.Fields.body).description("The body of a update post")
                            )
                            .build())
            ));
        }
    }

    @Nested
    @DisplayName("post 삭제")
    class PostDeleteTest {
        @Test
        void delete_success() throws Exception {

            // when
            var perform = mockMvc.perform(delete("/api/posts/{id}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .header(HttpHeaders.AUTHORIZATION, bearerToken)
            ).andDo(print());

            // then
            perform.andExpect(status().isOk());

            // docs
            perform.andDo(document("{class-name}/{method-name}",
                    resource(ResourceSnippetParameters.builder()
                            .tag(REST_DOC_TAG)
                            .summary("post 삭제 API")
                            .description("post 삭제 API")
                            .pathParameters(
                                    parameterWithName("id").description("The id of the post")
                            )
                            .build())));
        }
    }

}
