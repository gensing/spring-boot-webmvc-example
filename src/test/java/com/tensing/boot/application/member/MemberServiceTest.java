package com.tensing.boot.application.member;


import com.tensing.boot.application.member.dao.MemberEntityRepository;
import com.tensing.boot.application.member.model.data.MemberEntity;
import com.tensing.boot.application.member.model.dto.MemberDto;
import com.tensing.boot.application.member.service.MemberService;
import com.tensing.boot.config.AppConfiguration;
import com.tensing.boot.config.properties.PropertiesConfiguration;
import com.tensing.boot.global.filters.security.model.dto.SecurityDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(
        initializers = {
                ConfigDataApplicationContextInitializer.class, // application.yaml 사용 설정
        },
        classes = {
                MemberServiceTest.SpringTestConfig.class,
                PropertiesConfiguration.class,
                AppConfiguration.class,
        }
)
@ExtendWith(SpringExtension.class)
public class MemberServiceTest {

    @Configuration
    @ComponentScan(basePackageClasses = MemberServiceTest.class)
    public static class SpringTestConfig {
    }

    @Autowired
    MemberService memberServiceImpl;

    @MockBean
    MemberEntityRepository memberEntityRepository;

    @Nested
    @DisplayName("유저 생성")
    class MemberCreateMemberTest {
        @Test
        void createMember_success() throws Exception {

            //given
            final var memberRequest = MemberDto.MemberRequest.builder()
                    .username("tttt")
                    .password("tttt")
                    .build();

            final var memberEntity = MemberEntity.builder()
                    .id(1L)
                    .username(memberRequest.getUsername())
                    .password(memberRequest.getPassword())
                    .build();

            // mocking
            when(memberEntityRepository.save(any(MemberEntity.class))).thenReturn(memberEntity);

            //when
            final var memberResponse = memberServiceImpl.createMember(memberRequest);

            // then
            assertEquals(memberResponse.getId(), memberEntity.getId());
            assertEquals(memberResponse.getUsername(), memberEntity.getUsername());
            assertEquals(memberResponse.getEmail(), memberEntity.getEmail());
        }

    }

    @Nested
    @DisplayName("유저 확인")
    class MemberFindMemberTest {
        @Test
        void findMember_success() throws Exception {

            // given
            final var memberId = 1L;
            final var userInfo = SecurityDto.UserInfo.builder().id(memberId).build();
            final var memberEntity = MemberEntity.builder().id(memberId).username("tttt").email("tttt@tttt.tttt").build();

            // mocking
            when(memberEntityRepository.findById((any(Long.class)))).thenReturn(Optional.of(memberEntity));

            // when
            final var memberResponse = memberServiceImpl.findMember(memberId, userInfo);

            // then
            assertEquals(memberResponse.getId(), memberEntity.getId());
            assertEquals(memberResponse.getUsername(), memberEntity.getUsername());
            assertEquals(memberResponse.getEmail(), memberEntity.getEmail());
        }

    }

}
