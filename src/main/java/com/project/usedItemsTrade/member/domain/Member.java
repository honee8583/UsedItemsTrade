package com.project.usedItemsTrade.member.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    private boolean fromSocial;

    // JWT
    private String refresh_token;

    // email
    private String emailCode;
    private String passwordResetCode;

    public void updateRefreshToken(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
