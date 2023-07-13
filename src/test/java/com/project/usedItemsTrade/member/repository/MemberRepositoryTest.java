package com.project.usedItemsTrade.member.repository;

import com.project.usedItemsTrade.member.domain.Member;
import com.project.usedItemsTrade.member.domain.MemberStatus;
import com.project.usedItemsTrade.member.domain.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@DataJpaTest    // JPA 관련 설정을 자동으로 로드하고, 테스트에 필요한 빈들만 로드하여 성능 향상
class MemberRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    private Member createMember() {
        return Member.builder()
                .email("user@email.com")
                .password("1111")
                .name("user")
                .phone("010-1234-5678")
                .status(MemberStatus.AVAILABLE)
                .role(Role.USER)
                .fromSocial(false)
                .emailCode(UUID.randomUUID().toString())
                .emailAuthYn(false)
                .passwordResetKey(UUID.randomUUID().toString())
                .passwordResetLimitTime(LocalDateTime.now().plusDays(1))
                .build();
    }

    @Test
    void testFindByEmail() {
        // given
        Member member = createMember();

        entityManager.persist(member);
        entityManager.flush();

        // when
        Optional<Member> optionalMember = memberRepository.findByEmail(member.getEmail());
        Member foundMember = optionalMember.get();

        // then
        assertEquals("user@email.com", foundMember.getEmail());
        assertEquals("user", foundMember.getName());
        assertEquals("010-1234-5678", foundMember.getPhone());
        assertFalse(foundMember.isFromSocial());
        assertEquals("1111", foundMember.getPassword());
        assertEquals(Role.USER, foundMember.getRole());
    }

    @Test
    void testFindByEmailCode() {
        // given
        Member member = createMember();
        entityManager.persist(member);
        entityManager.flush();

        // when
        Optional<Member> optionalMember =
                memberRepository.findByEmailCode(member.getEmailCode());
        Member foundMember = optionalMember.get();

        // then
        assertEquals(member.getEmail(), foundMember.getEmail());
    }

    @Test
    void testFindByPasswordResetKey() {
        // given
        Member member = createMember();
        entityManager.persist(member);
        entityManager.flush();

        // when
        Optional<Member> optionalMember =
                memberRepository.findByPasswordResetKey(member.getPasswordResetKey());
        Member foundMember = optionalMember.get();

        // then
        assertEquals(member.getEmail(),foundMember.getEmail());
    }

    @Test
    void testFindByEmailAndIsSocial() {
        // given
        Member member = createMember();

        entityManager.persist(member);
        entityManager.flush();

        // when
        Optional<Member> optionalMember =
                memberRepository.findByEmailAndFromSocial("user@email.com", false);
        Member foundMember = optionalMember.get();
        // then
        assertFalse(foundMember.isFromSocial());
        assertEquals(foundMember.getName(), "user");
        assertEquals(foundMember.getPhone(), "010-1234-5678");
        assertEquals(foundMember.getPassword(), "1111");
        assertEquals(foundMember.getRole(), Role.USER);
    }
}