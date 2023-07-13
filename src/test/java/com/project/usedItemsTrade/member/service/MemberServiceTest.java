package com.project.usedItemsTrade.member.service;

import com.project.usedItemsTrade.member.domain.*;
import com.project.usedItemsTrade.member.repository.MemberRepository;
import com.project.usedItemsTrade.member.service.impl.MemberServiceImpl;
import com.project.usedItemsTrade.member.utils.MailUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailUtil mailUtil;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member createMember() {
        return Member.builder()
                .email("user@email.com")
                .password(passwordEncoder.encode("1111"))
                .name("user")
                .phone("010-1234-5678")
                .status(MemberStatus.REQ)
                .role(Role.USER)
                .fromSocial(false)
                .emailCode("1111")
                .emailAuthYn(false)
                .build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void testJoin() {
        // given
        MemberRequestDto.MemberJoinDto joinDto =
                MemberRequestDto.MemberJoinDto.builder()
                        .email("user@email.com")
                        .password("1111")
                        .name("user")
                        .phone("010-1234-5678")
                        .build();

        // when
        memberService.join(joinDto);

        // then
        verify(mailUtil, times(1)).sendMail(anyString(), anyString(), anyString());

        verify(memberRepository, times(1)).save(any(Member.class));
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberCaptor.capture());

        Member savedMember = memberCaptor.getValue();
        System.out.println(savedMember);
        assertEquals(savedMember.getEmail(), joinDto.getEmail());
        assertEquals(savedMember.getName(), joinDto.getName());
        assertEquals(savedMember.getPhone(), joinDto.getPhone());
    }

    @Test
    @DisplayName("비밀번호 초기화 이메일 발송 구현")
    void testSendPasswordResetMail() {
        // given
        Member member = createMember();

        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));

        // when
        memberService.sendPasswordResetMail(member.getEmail());

        // then
        verify(mailUtil, times(1)).sendMail(anyString(), anyString(), anyString());

        verify(memberRepository, times(1)).save(any(Member.class));
        ArgumentCaptor<Member> argumentCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(argumentCaptor.capture());

        Member savedMember = argumentCaptor.getValue();
        assertFalse(savedMember.getPasswordResetKey().isEmpty());
    }

    @Test
    @DisplayName("회원정보 불러오기 테스트")
    void testMyInfo() {
        // given
        Member member = createMember();

        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));

        // when
        MemberResponseDto.MemberInfoDto memberInfoDto = memberService.myInfo(member.getEmail());

        // then
        assertEquals(memberInfoDto.getEmail(), member.getEmail());
        assertEquals(memberInfoDto.getName(), member.getName());
        assertEquals(memberInfoDto.getPhone(), member.getPhone());
        assertEquals(memberInfoDto.getStatus(), member.getStatus());
    }

    @Test
    @DisplayName("회원정보 수정 테스트")
    void testUpdateMyInfo() {
        // given
        Member member = createMember();

        MemberRequestDto.MemberUpdateInfoDto updateInfoDto =
                MemberRequestDto.MemberUpdateInfoDto.builder()
                        .email(member.getEmail())
                        .name("modifiedUser")
                        .phone("010-3333-3333")
                        .build();

        given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

        // when
        memberService.updateMyInfo(updateInfoDto);

        // then
        verify(memberRepository, times(1)).save(any(Member.class));
        ArgumentCaptor<Member> memberArgumentCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberArgumentCaptor.capture());

        Member savedMember = memberArgumentCaptor.getValue();
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(updateInfoDto.getName(), savedMember.getName());
        assertEquals(updateInfoDto.getPhone(), savedMember.getPhone());
    }

    @Test
    @DisplayName("이메일 인증 테스트")
    void testEmailAuth() {
        // given
        Member member = createMember();
        String emailCode = "1111";

        given(memberRepository.findByEmailCode(anyString())).willReturn(Optional.of(member));

        // when
        memberService.emailAuth(emailCode);

        // then
        verify(memberRepository, times(1)).save(any(Member.class));
        ArgumentCaptor<Member> argumentCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(argumentCaptor.capture());

        Member savedMember = argumentCaptor.getValue();
        assertTrue(savedMember.isEmailAuthYn());
        assertEquals(savedMember.getStatus(), MemberStatus.AVAILABLE);
    }

    @Test
    @DisplayName("회원탈퇴 테스트")
    public void testWithdraw_Success() {
        when(passwordEncoder.encode("1111"))
                .thenReturn("$2a$10$k38W/DZkkQ.58il6i2yYz.J0pyaxo/EXDXZHALB9OnOVbvhiWgkC6");

        Member member = createMember();

        MemberRequestDto.MemberWithdrawDto withdrawDto =
                MemberRequestDto.MemberWithdrawDto
                        .builder()
                        .email("user@email.com")
                        .password("1111")
                        .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));

        // when
        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);
        memberService.withdraw(withdrawDto);

        // then
        verify(memberRepository, times(1))
                .save(any(Member.class));
    }
    // TODO : 예외처리 테스트 코드 작성 필요

}