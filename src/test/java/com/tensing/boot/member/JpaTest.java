package com.tensing.boot.member;

import com.tensing.boot.application.member.model.vo.entity.MemberEntity;
import com.tensing.boot.application.member.dao.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.util.AssertionErrors.assertEquals;




@ActiveProfiles("test")
@SpringBootTest // @DataJpaTest 사용시 불러오지 못하는 빈으로 인하여, 잠시 @SpringBootTest 사용
public class JpaTest {

    @Autowired
    private MemberRepository memberRepositoryImpl;

    @Test
    void save() {

        // given
//        final MemberEntity memberEntity = MemberEntity.builder().username("boot").password("boot1234").email("boot@boot.com").build();
//
//        // when
//        final MemberEntity saveMemberEntity = memberRepositoryImpl.save(memberEntity);
//
//        // then
//        assertEquals("username 이 다릅니다.", memberEntity.getUsername(), saveMemberEntity.getUsername());
//        assertEquals("email 이 다릅니다.", memberEntity.getEmail(), memberEntity.getEmail());
//        assertEquals("비밀번호가 다릅니다.", memberEntity.getPassword(), memberEntity.getPassword());
    }
}
