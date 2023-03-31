package com.tensing.boot;

import com.tensing.boot.application.member.controller.MemberController;
import com.tensing.boot.application.post.controller.PostController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class BootApplicationTests {

    @Autowired
    private MemberController memberController;
    @Autowired
    private PostController postController;

    @Test
    void contextLoads() {
        assertThat(memberController).isNotNull();
        assertThat(postController).isNotNull();
    }

}
