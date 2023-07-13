package com.project.usedItemsTrade.member.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtCheckFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        log.info(">>> JwtCheckFilter");

        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. 매 요청의 헤더에 토큰이 있는지 확인
        String token = resolveTokenFromRequest(request);

        // 2. 토큰 검증 후 Authentication -> SecurityContext에 삽입
        // UserDetailsService 구현 클래스 호출
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            // 토큰 유효성 검증
            Authentication authentication = jwtTokenProvider.getAuthentication(token);    // UserDetailsService 호출

            // 사용자명 -> 요청경로명
            log.info(String.format("[%s] -> %s", jwtTokenProvider.getUsername(token), request.getRequestURI()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            throw new RuntimeException(">> No tokens...");
        }

        filterChain.doFilter(request, response);
    }

    private String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);     // 헤더에서 키("Authorization")에 해당하는 value 값을 추출

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());  // "Bearer"뒤의 토큰 부분을 떼어내서 반환
        }

        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String[] excludePath = {"/login", "/member/", "/swagger", "/v2/"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}
