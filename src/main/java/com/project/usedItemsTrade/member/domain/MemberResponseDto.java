package com.project.usedItemsTrade.member.domain;

import lombok.*;

public class MemberResponseDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class MemberInfoDto {
        private String email;
        private String name;
        private String phone;
        private MemberStatus status;  // TODO 넣을지 말지

        public static MemberInfoDto memberToInfoDto(Member member) {
            return MemberInfoDto.builder()
                    .email(member.getEmail())
                    .name(member.getName())
                    .phone(member.getPhone())
                    .status(member.getStatus())
                    .build();
        }
    }
}
