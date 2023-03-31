package com.tensing.boot.application.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tensing.boot.application.member.dao.MemberEntityRepository;
import com.tensing.boot.application.member.model.dto.MemberDto;
import com.tensing.boot.application.member.service.MemberService;
import com.tensing.boot.application.post.dao.PostEntityRepository;
import com.tensing.boot.application.post.model.dto.PostDto;
import com.tensing.boot.application.post.service.PostService;
import com.tensing.boot.global.filters.security.Const;
import com.tensing.boot.global.filters.security.model.dto.SecurityDto;
import com.tensing.boot.global.filters.security.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
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

    @Autowired
    private MemberEntityRepository memberEntityRepository;

    @Autowired
    private PostEntityRepository postEntityRepository;

    private String bearerToken;

    private PostDto.PostRequest postRequest = PostDto.PostRequest.builder()
            .title("test title 1")
            .body("test body 1")
            .build();

    private long postId;


    @BeforeEach
    void init() {

        log.info("before each");
        var memberRequest = MemberDto.MemberRequest.builder()
                .username("test1234")
                .email("test1234@test.test")
                .password("test1234@T")
                .build();

        var memberId = memberService.createMember(memberRequest);

        var tokenRequest = SecurityDto.TokenRequest.builder()
                .grantType(SecurityDto.GranType.ISSUE)
                .username(memberRequest.getUsername())
                .password(memberRequest.getPassword())
                .build();

        this.bearerToken = Const.BEARER_PREFIX + securityService.getToken(tokenRequest).getAccessToken();

        this.postId = postService.insert(postRequest, SecurityDto.UserInfo.builder().id(memberId).build());
    }


    @AfterEach
    void tearDown() {
        postEntityRepository.deleteAll();
        memberEntityRepository.deleteAll();
    }

    @Test
    @DisplayName("post 생성")
    void createTest() throws Exception {

        // when
        var postRequest = PostDto.PostRequest.builder().title("test title 1").body("test body 1").build();

        var perform = mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .header(HttpHeaders.AUTHORIZATION + "2", bearerToken)
                .content(objectMapper.writeValueAsString(postRequest))
        );

        // then
        perform.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("post list 조회")
    void getListTest() throws Exception {

        // when
        var perform = mockMvc.perform(get("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        perform.andExpect(status().isOk())
        //.andExpect(jsonPath("$.*", hasSize(2)))
        ;
    }

    @Test
    @DisplayName("post 조회")
    void getTest() throws Exception {

        // when
        var perform = mockMvc.perform(get("/api/posts/{postId}", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
        );

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath(PostDto.PostResponse.Fields.id).value(postId))
                .andExpect(jsonPath(PostDto.PostResponse.Fields.title).value(postRequest.getTitle()))
                .andExpect(jsonPath(PostDto.PostResponse.Fields.body).value(postRequest.getBody()));
    }

    @Test
    @DisplayName("post 수정")
    void putTest() throws Exception {

        // when
        var postRequest = PostDto.PostPutRequest.builder().title("test update title 1").body("test update body 1").build();

        var perform = mockMvc.perform(put("/api/posts/{postId}", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .content(objectMapper.writeValueAsString(postRequest))
        );

        // then
        perform.andExpect(status().isOk());
    }

    @Test
    @DisplayName("post 삭제")
    void deleteTest() throws Exception {

        // when
        var perform = mockMvc.perform(delete("/api/posts/{postId}", postId)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
        );

        // then
        perform.andExpect(status().isOk());
    }

}
