package com.project.usedItemsTrade.member.controller;

import com.project.usedItemsTrade.member.domain.JwtToken;
import com.project.usedItemsTrade.member.domain.MemberRequestDto;
import com.project.usedItemsTrade.member.service.LoginService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    // 카카오로그인
    @PostMapping("/kakaoLogin")
    @ApiOperation(value = "카카오로그인", notes = "kakaoLogin")
    public ResponseEntity<JwtToken> kakaoLogin(@RequestBody MemberRequestDto.KakaoMemberLoginDto loginDto) throws Exception {
        JwtToken tokens = loginService.kakaoLogin(loginDto);

        return ResponseEntity.ok(tokens);
    }

    // 일반로그인
    @PostMapping("/normal")
    @ApiOperation(value = "일반로그인", notes = "normalLogin")
    public ResponseEntity<JwtToken> normalLogin(@Valid @RequestBody MemberRequestDto.NormalMemberLoginDto loginDto) {
        JwtToken tokens = loginService.normalLogin(loginDto);

        return ResponseEntity.ok(tokens);
    }

    // JWT 토큰 갱신
    @PostMapping("/refresh")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "JWT토큰 재발급", notes = "Refresh Jwt Tokens")
    public ResponseEntity<JwtToken> refreshToken(@RequestBody JwtToken refreshToken) {
        log.info(refreshToken.toString());

        JwtToken newTokens = loginService.generateNewTokens(refreshToken.getRefreshToken());

        return ResponseEntity.ok(newTokens);
    }
}
