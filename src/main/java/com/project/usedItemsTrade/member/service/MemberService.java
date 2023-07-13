package com.project.usedItemsTrade.member.service;

import com.project.usedItemsTrade.member.domain.MemberRequestDto;
import com.project.usedItemsTrade.member.domain.MemberResponseDto;

public interface MemberService {
    // 회원가입(메일발송)
    void join(MemberRequestDto.MemberJoinDto joinDto);

    // 비밀번호 초기화(메일발송)
    void sendPasswordResetMail(String email);

    // 회원정보
    MemberResponseDto.MemberInfoDto myInfo(String email);

    // 회원정보수정
    void updateMyInfo(MemberRequestDto.MemberUpdateInfoDto updateInfoDto);

    // 회원가입 이메일 인증
    void emailAuth(String emailAuthCode);

    // 비밀번호 초기화 이메일 인증
    void resetPassword(MemberRequestDto.ResetPasswordDto resetPasswordDto);

    // 회원탈퇴
    void withdraw(MemberRequestDto.MemberWithdrawDto withdrawDto);
}
