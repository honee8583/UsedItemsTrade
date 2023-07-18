package com.project.usedItemsTrade.member.service.impl;

import com.project.usedItemsTrade.member.domain.*;
import com.project.usedItemsTrade.member.error.exception.*;
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
            throw new AlreadyExistMemberException();
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
                member.getName() + "님 회원가입에 성공하였습니다!!",
                "링크");  // TODO LINK

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void sendPasswordResetMail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(UserNotExistException::new);

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
                .orElseThrow(UserNotExistException::new);

        return MemberResponseDto.MemberInfoDto.memberToInfoDto(member);
    }

    @Override
    @Transactional
    public void updateMyInfo(MemberRequestDto.MemberUpdateInfoDto updateInfoDto) {
        Member member = memberRepository.findByEmail(updateInfoDto.getEmail())
                        .orElseThrow(UserNotExistException::new);

        member.updateMemberInfo(updateInfoDto);

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void emailAuth(String emailAuthCode) {
        Member member = memberRepository.findByEmailCode(emailAuthCode)
                .orElseThrow(UserNotExistException::new);

        if (member.isEmailAuthYn()) {
            throw new AlreadyEmailAuthenticatedException();
        }

        member.updateEmailAuth();

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void resetPassword(MemberRequestDto.ResetPasswordDto resetPasswordDto) {
        Member member = memberRepository.findByPasswordResetKey(resetPasswordDto.getResetPasswordKey())
                .orElseThrow(UserNotExistException::new);

        if (member.getPasswordResetLimitTime() == null
                || member.getPasswordResetLimitTime().isBefore(LocalDateTime.now())) {
            throw new PasswordResetLimitTimeInvalidException();
        }

        String newEncodedPwd = passwordEncoder.encode(resetPasswordDto.getNewPwd());
        member.resetPassword(newEncodedPwd);
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void withdraw(MemberRequestDto.MemberWithdrawDto withdrawDto) {
        Member member = memberRepository.findByEmail(withdrawDto.getEmail())
                .orElseThrow(UserNotExistException::new);

        if (!passwordEncoder.matches(withdrawDto.getPassword(), member.getPassword())) {
            throw new PasswordNotMatchException();
        }

        member.withdraw();
        memberRepository.save(member);
    }
}
