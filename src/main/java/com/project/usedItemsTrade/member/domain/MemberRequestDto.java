package com.project.usedItemsTrade.member.domain;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


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
        @Email(message = "아이디는 이메일 형식입니다")
        private String email;
        @Min(value = 8, message = "비밀번호는 최소 8자리입니다")
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class MemberJoinDto {
        @Email(message = "아이디는 이메일 형식입니다")
        private String email;

        @Min(value = 8, message = "비밀번호는 최소 8자리입니다")
        private String password;

        @NotBlank(message = "이름 필드의 값을 채우셔야 합니다")
        private String name;

        @NotBlank(message = "전화번호 필드의 값을 채우셔야 합니다")
        private String phone;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class MemberUpdateInfoDto {
        @Email(message = "아이디는 이메일 형식입니다")
        private String email;
        private String name;
        private String phone;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ResetPasswordDto {
        @Min(value = 8, message = "비밀번호는 최소 8자리입니다")
        private String newPwd;
        private String resetPasswordKey;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class MemberWithdrawDto {
        @Email(message = "아이디는 이메일 형식입니다")
        private String email;
        @Min(value = 8, message = "비밀번호는 최소 8자리입니다")
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class EmailDto {
        @Email(message = "아이디는 이메일 형식입니다")
        private String email;
        private String emailAuthCode;
    }
}
