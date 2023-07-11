package com.project.usedItemsTrade.member.service;

import com.project.usedItemsTrade.member.domain.JwtToken;
import com.project.usedItemsTrade.member.domain.Member;
import com.project.usedItemsTrade.member.domain.MemberRequestDto;
import com.project.usedItemsTrade.member.domain.Role;
import com.project.usedItemsTrade.member.jwt.JwtTokenProvider;
import com.project.usedItemsTrade.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;   // 만료시간 -> 1 hour
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 15 * 24 * 1000 * 60 * 60; // 15 days
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String KAKAO_LOGIN_PWD = "1111";

    // 카카오 회원 조회 없으면 저장 및 반환
    @Transactional
    public Member saveKakaoMemberIfNotExists(MemberRequestDto.KakaoMemberLoginDto loginDto) {
        // 카카오회원 조회
        log.info(">> KakaoLoginDTO : " + loginDto.getEmail());

        Optional<Member> optionalMember = memberRepository.findByEmailAndFromSocial(loginDto.getEmail(), true);

        if (!optionalMember.isPresent()) {  // 회원이 존재하지 않을경우 저장
            log.info(">> 카카오로 처음 로그인하므로 회원가입합니다.");
            Member member = Member.builder()
                    .email(loginDto.getEmail())
                    .password(passwordEncoder.encode(KAKAO_LOGIN_PWD))
                    .name(loginDto.getNickname())
                    .role(Role.USER)
                    .fromSocial(true)
                    .build();

            return memberRepository.save(member);
        }

        return optionalMember.get();    // 조회한 회원 반환
    }

    // 카카오 로그인
    @Transactional
    public JwtToken kakaoLogin(MemberRequestDto.KakaoMemberLoginDto loginDto) throws Exception {
        Member member = saveKakaoMemberIfNotExists(loginDto);

        List<SimpleGrantedAuthority> roles =
                Arrays.asList(new SimpleGrantedAuthority("ROLE_" + member.getRole()));
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(member.getEmail(), roles, TOKEN_EXPIRE_TIME);
        String refreshJwt = TOKEN_PREFIX + tokenProvider.generateToken(member.getEmail(),
                roles, REFRESH_TOKEN_EXPIRE_TIME);

        log.info(">> 소셜 로그인 JWT 토큰: " + jwt);
        log.info(">> 소셜 로그인 REFRESH 토큰: " + refreshJwt);

        member.updateRefreshToken(refreshJwt);
        memberRepository.save(member);

        authenticate(member.getEmail(), KAKAO_LOGIN_PWD);    // 로그인처리

        return new JwtToken(jwt, refreshJwt);
    }

    // 일반로그인
    @Transactional
    public JwtToken normalLogin(MemberRequestDto.NormalMemberLoginDto loginDTO) {
        Optional<Member> optionalMember = memberRepository.findByEmailAndFromSocial(loginDTO.getEmail(), false);

        if (!optionalMember.isPresent()) {
            throw new UsernameNotFoundException("no such user..");
        }

        Member member = optionalMember.get();

        if (!passwordEncoder.matches(loginDTO.getPassword(), member.getPassword())) {
            throw new RuntimeException("Password doesn't match!");  // TODO 커스텀 예외 처리
        }

        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()));

        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(loginDTO.getEmail(), roles, TOKEN_EXPIRE_TIME);
        String refreshJwt = TOKEN_PREFIX + tokenProvider.generateToken(loginDTO.getEmail(),
                roles, REFRESH_TOKEN_EXPIRE_TIME);

        log.info(">> 일반 로그인 JWT 토큰: " + jwt);
        log.info(">> 일반 로그인 REFRESH 토큰: " + refreshJwt);

        member.updateRefreshToken(refreshJwt);
        memberRepository.save(member);

        return new JwtToken(jwt, refreshJwt);
    }

    @Transactional
    public JwtToken generateNewTokens(String refreshToken) {
        log.info(">> 토큰이 만료되었으므로 재생성합니다");
        refreshToken = refreshToken.substring(TOKEN_PREFIX.length());

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException(">> 토큰이 유효하지 않습니다!");    // TODO Create Custom Exception
        }

        String username = tokenProvider.getUsername(refreshToken);
        List<SimpleGrantedAuthority> roles = tokenProvider.getRoles(refreshToken);


        String newJwt = TOKEN_PREFIX + tokenProvider.generateToken(username, roles, TOKEN_EXPIRE_TIME);
        String newRefreshJwt = TOKEN_PREFIX + tokenProvider.generateToken(username, roles, REFRESH_TOKEN_EXPIRE_TIME);

        log.info(">> 재생성된 jwt 토큰: " + newJwt);
        log.info(">> 재생성된 REFRESH 토큰: " + newRefreshJwt);

        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member member = optionalMember.get();
        member.updateRefreshToken(newRefreshJwt);
        memberRepository.save(member);

        return new JwtToken(newJwt, newRefreshJwt);
    }

    // 강제 로그인 처리
    public void authenticate(String username, String pwd) throws Exception {
        try {
            log.info(">> 강제 로그인 처리 ");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, pwd));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
