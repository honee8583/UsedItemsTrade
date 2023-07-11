package com.project.usedItemsTrade.member.service;

import com.project.usedItemsTrade.member.domain.Member;
import com.project.usedItemsTrade.member.domain.MemberDto;
import com.project.usedItemsTrade.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username: " + username);

        Member member = memberRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("No Such User.."));

        MemberDto memberDto = new MemberDto(member.getEmail(), member.getPassword(), false, member.getName(),
                Arrays.asList(member.getRole()).stream().map((role) -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList()));

        log.info("MemberDto : " + memberDto);

        return memberDto;
    }
}
