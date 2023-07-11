package com.project.usedItemsTrade.member.service;

import com.project.usedItemsTrade.member.domain.JwtToken;
import com.project.usedItemsTrade.member.domain.Member;
import com.project.usedItemsTrade.member.domain.MemberRequestDto;
import com.project.usedItemsTrade.member.domain.Role;
import com.project.usedItemsTrade.member.jwt.JwtTokenProvider;
import com.project.usedItemsTrade.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class LoginServiceTest {

    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 15 * 24 * 1000 * 60 * 60;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private LoginService loginService;

    private Member createMember() {
        return Member.builder()
                .email("user@email.com")
                .password("1111")
                .name("user")
                .role(Role.USER)
                .fromSocial(true)
                .build();
    }

    @Test
    void testSaveKakaoMemberIfNotExists() {
        // given
        Member member = createMember();

        MemberRequestDto.KakaoMemberLoginDto loginDto = MemberRequestDto.KakaoMemberLoginDto.builder()
                .email("user@email.com")
                .nickname("user")
                .build();

        given(memberRepository.save(any())).willReturn(member);

        // when
        Member foundMember = loginService.saveKakaoMemberIfNotExists(loginDto);

        // then
        assertEquals("user@email.com", foundMember.getEmail());
        assertEquals("user", foundMember.getName());
        assertEquals("1111", foundMember.getPassword());
        assertTrue(foundMember.isFromSocial());
    }

    @Test
    void testKakaoLogin() throws Exception {
        // given
        Member member = createMember();
        MemberRequestDto.KakaoMemberLoginDto loginDto =
                MemberRequestDto.KakaoMemberLoginDto.builder()
                    .email("user@email.com")
                    .nickname("user")
                    .build();

        List<SimpleGrantedAuthority> roles =
                Arrays.asList(new SimpleGrantedAuthority("ROLE_" + member.getRole()));

        given(memberRepository.save(any())).willReturn(member);

        // when
        loginService.kakaoLogin(loginDto);

        // then
        verify(tokenProvider).generateToken(member.getEmail(), roles, TOKEN_EXPIRE_TIME);
        verify(tokenProvider).generateToken(member.getEmail(), roles, REFRESH_TOKEN_EXPIRE_TIME);
    }

    @Test
    void testNormalLogin() {
        // given
        Member member = Member.builder()
                .email("user@email.com")
                .password("$2a$10$dv8gPW9bszl5PegdQqDEweYcM.bMN9aIma1yL594R2RQXfMEFM6eK")
                .name("user")
                .role(Role.USER)
                .fromSocial(true)
                .build();

        MemberRequestDto.NormalMemberLoginDto loginDto =
                MemberRequestDto.NormalMemberLoginDto.builder()
                    .email(member.getEmail())
                    .password("1111")
                    .build();

        List<SimpleGrantedAuthority> roles =
                Arrays.asList(new SimpleGrantedAuthority("ROLE_" + member.getRole()));

        given(memberRepository.findByEmailAndFromSocial(anyString(), anyBoolean())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

        // when
        JwtToken tokens = loginService.normalLogin(loginDto);

        // then
        verify(passwordEncoder, times(1)).matches("1111", member.getPassword());
        verify(tokenProvider).generateToken(member.getEmail(), roles, TOKEN_EXPIRE_TIME);
        verify(tokenProvider).generateToken(member.getEmail(), roles, REFRESH_TOKEN_EXPIRE_TIME);
    }
}