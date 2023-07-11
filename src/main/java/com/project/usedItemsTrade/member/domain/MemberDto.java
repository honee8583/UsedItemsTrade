package com.project.usedItemsTrade.member.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class MemberDto extends User {
    private String username;
    private String password;
    private String name;
    private boolean fromSocial;

    public MemberDto(String username, String password, boolean fromSocial, String name,
                     Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.username = username;
        this.password = password;
        this.name = name;
        this.fromSocial = fromSocial;
    }
}
