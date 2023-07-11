package com.project.usedItemsTrade.member.jwt;

import com.project.usedItemsTrade.member.domain.JwtToken;
import com.project.usedItemsTrade.member.service.MemberDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final MemberDetailsService memberDetailsService;

    private static final String KEY_ROLES = "roles";

    @Value("{spring.jwt.secret}")
    private String secretKey;

    // 아이디, 권한으로 토큰 생성
    public String generateToken(String username, List<SimpleGrantedAuthority> roles, Long time) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles);

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + time);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)               // 생성시간
                .setExpiration(expiredDate)     // 만료시간
                .signWith(SignatureAlgorithm.HS512, this.secretKey)     // 전자서명
                .compact();
    }

    // 인증정보를 가져와 스프링에서 지원하는 토큰으로 반환
    public Authentication getAuthentication(String jwt) {   // jwt 토큰으로부터 인증정보(토큰)를 가져오는 메소드
        UserDetails userDetails = this.memberDetailsService.loadUserByUsername(getUsername(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails, ""
                , userDetails.getAuthorities());    // 스프링에서 지원하는 토큰(사용자정보, 사용자권한정보를 포함하게 됨)
    }

    // 토큰으로부터 로그인한 유저의 아이디 추출
    public String getUsername(String token) {
        return this.parseClaims(token).getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token))
            return false;  // token 이 존재하지 않을경우

        Claims claims = parseClaims(token);
        return !claims.getExpiration().before(new Date());   // 만료시간이 현재시간보다 전일경우(즉, 지났을경우)
    }

    public List<SimpleGrantedAuthority> getRoles(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        List<SimpleGrantedAuthority> roles = (List<SimpleGrantedAuthority>) claims.get(KEY_ROLES);

        return roles;
    }

    // Claim 정보 추출
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(this.secretKey)
                    .parseClaimsJws(token)
                    .getBody();   // Claim 정보 반환
        } catch(ExpiredJwtException e) {
            // 만료시간이 지난경우 ExpiredJwtException 이 발생

            return e.getClaims();
        }
    }
}
