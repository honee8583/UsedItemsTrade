package com.project.usedItemsTrade.member.service.impl;

import com.project.usedItemsTrade.member.domain.*;
import com.project.usedItemsTrade.member.repository.MemberRepository;
import com.project.usedItemsTrade.member.service.MemberService;
import com.project.usedItemsTrade.member.utils.MailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailUtil mailUtil;

    @Override
    @Transactional
    public void join(MemberRequestDto.MemberJoinDto joinDto) {
        String emailCode = UUID.randomUUID().toString();

        if (memberRepository.findByEmail(joinDto.getEmail()).isPresent()) {
            throw new RuntimeException("User Already exists");  // TODO Custom Exception
        }

        Member member = Member.builder()
                .email(joinDto.getEmail())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .name(joinDto.getName())
                .phone(joinDto.getPhone())
                .status(MemberStatus.REQ)
                .role(Role.USER)
                .fromSocial(false)
                .emailCode(emailCode)
                .emailAuthYn(false)
                .build();

        mailUtil.sendMail(
                member.getEmail(),
                "회원가입에 성공하였습니다!!",
                "링크" // TODO : 링크 설정
        );

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void sendPasswordResetMail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No such user.."));

        String resetKey = UUID.randomUUID().toString();
        member.updatePasswordResetKey(resetKey, LocalDateTime.now().plusDays(1));

        mailUtil.sendMail(email, "비밀번호 초기화 코드입니다."
        ,"초기화 코드 : " + resetKey);

        memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponseDto.MemberInfoDto myInfo(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No such user.."));

        return MemberResponseDto.MemberInfoDto.memberToInfoDto(member);
    }

    @Override
    @Transactional
    public void updateMyInfo(MemberRequestDto.MemberUpdateInfoDto updateInfoDto) {
        Member member = memberRepository.findByEmail(updateInfoDto.getEmail())
                        .orElseThrow(() -> new RuntimeException("No such user..."));

        member.updateMemberInfo(updateInfoDto);

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void emailAuth(String emailAuthCode) {
        Member member = memberRepository.findByEmailCode(emailAuthCode)
                .orElseThrow(() -> new RuntimeException("No Such EmailCode exists..."));

        if (member.isEmailAuthYn()) {
            throw new RuntimeException("Already Email Authenticated!!");   // TODO Custom Exception
        }

        member.updateEmailAuth();

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void resetPassword(MemberRequestDto.ResetPasswordDto resetPasswordDto) {
        Member member = memberRepository.findByPasswordResetKey(resetPasswordDto.getResetPasswordKey())
                .orElseThrow(() -> new RuntimeException("Wrong passwordKey!!"));

        if (member.getPasswordResetLimitTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("비밀번호 초기화 유효시간이 지났습니다!");
        }

        if (member.getPasswordResetLimitTime() == null) {
            throw new RuntimeException("비밀번호 초기화 유효시간이 존재하지 않습니다!");
        }

        String newEncodedPwd = passwordEncoder.encode(resetPasswordDto.getNewPwd());
        member.resetPassword(newEncodedPwd);
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void withdraw(MemberRequestDto.MemberWithdrawDto withdrawDto) {
        Member member = memberRepository.findByEmail(withdrawDto.getEmail())
                .orElseThrow(() -> new RuntimeException("No Such User..."));    // TODO Custom Exception

        if (!passwordEncoder.matches(member.getPassword(), withdrawDto.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다!"); // TODO Custom Exception
        }

        member.withdraw();
        memberRepository.save(member);
    }
}
