package com.project.usedItemsTrade.member.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String name;

    private String phone;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    private boolean fromSocial;

    // JWT
    private String refresh_token;

    // email
    private String emailCode;
    private boolean emailAuthYn;

    // reset password
    private String passwordResetKey;
    private LocalDateTime passwordResetLimitTime;

    public void updateRefreshToken(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void updateMemberInfo(MemberRequestDto.MemberUpdateInfoDto updateInfoDto) {
        this.name = updateInfoDto.getName();
        this.phone = updateInfoDto.getPhone();
    }

    public void updateEmailAuth() {
        this.emailAuthYn = true;
        this.status = MemberStatus.AVAILABLE;
    }

    public void updatePasswordResetKey(String passwordResetKey, LocalDateTime limitTime) {
        this.passwordResetKey = passwordResetKey;
        this.passwordResetLimitTime = limitTime;
    }

    public void resetPassword(String newPwd) {
        this.password = newPwd;
    }

    public void withdraw() {
        this.password = null;
        this.phone = null;
        this.emailAuthYn = false;
        this.status = MemberStatus.WITHDRAW;
        this.role = null;
        this.refresh_token = null;
    }
}
