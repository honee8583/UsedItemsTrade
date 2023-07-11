package com.project.usedItemsTrade.member.controller;

import com.project.usedItemsTrade.member.domain.JwtToken;
import com.project.usedItemsTrade.member.domain.MemberRequestDto;
import com.project.usedItemsTrade.member.service.LoginService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    // 카카오로그인
    @ApiOperation(value = "카카오로그인", notes = "kakaoLogin")
    @PostMapping("/kakaoLogin")
    public ResponseEntity<JwtToken> kakaoLogin(@RequestBody MemberRequestDto.KakaoMemberLoginDto loginDto) throws Exception {
        JwtToken tokens = loginService.kakaoLogin(loginDto);

        return ResponseEntity.ok(tokens);
    }

    // 일반로그인
    @ApiOperation(value = "일반로그인", notes = "normalLogin")
    @PostMapping("/login")
    public ResponseEntity<JwtToken> normalLogin(@RequestBody MemberRequestDto.NormalMemberLoginDto loginDto) {
        JwtToken tokens = loginService.normalLogin(loginDto);

        return ResponseEntity.ok(tokens);
    }

    // JWT 토큰 갱신
    @ApiOperation(value = "JWT토큰 재발급", notes = "Refresh Jwt Tokens")
    @PostMapping("/refresh")
    public ResponseEntity<JwtToken> refreshToken(@RequestBody JwtToken refreshToken) {
        log.info(refreshToken.toString());

        JwtToken newTokens = loginService.generateNewTokens(refreshToken.getRefreshToken());

        return ResponseEntity.ok(newTokens);
    }
}
