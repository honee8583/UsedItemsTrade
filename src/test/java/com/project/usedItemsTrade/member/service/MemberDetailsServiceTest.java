package com.project.usedItemsTrade.member.service;

import com.project.usedItemsTrade.member.domain.Member;
import com.project.usedItemsTrade.member.domain.MemberDto;
import com.project.usedItemsTrade.member.domain.Role;
import com.project.usedItemsTrade.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberDetailsServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberDetailsService memberDetailsService;

    @Test
    void testLoadUserByUsername() {
        // given
        Member member = Member.builder()
                .email("test@email.com")
                .name("user1")
                .password("1111")
                .phone("010-1234-5678")
                .role(Role.USER)
                .fromSocial(false)
                .build();

        when(memberRepository.findByEmailAndFromSocial("test@email.com", false)).thenReturn(Optional.of(member));

        // when
        MemberDto memberDto = (MemberDto) memberDetailsService.loadUserByUsername("test@email.com");

        // then
        assertEquals("1111", memberDto.getPassword());
        assertEquals("user1", memberDto.getName());
        assertFalse(memberDto.isFromSocial());
    }
}