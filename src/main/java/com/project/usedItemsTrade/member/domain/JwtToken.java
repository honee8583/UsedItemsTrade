package com.project.usedItemsTrade.member.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JwtToken {
    private String jwt;
    private String refreshToken;
}
