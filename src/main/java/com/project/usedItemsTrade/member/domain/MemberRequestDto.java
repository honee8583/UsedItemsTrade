package com.project.usedItemsTrade.member.domain;

import lombok.*;

public class MemberRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class KakaoMemberLoginDto {
        private String email;
        private String nickname;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class NormalMemberLoginDto {
        private String email;
        private String password;
    }
}
