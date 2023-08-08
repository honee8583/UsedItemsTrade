package com.project.usedItemsTrade.member.controller;

import com.project.usedItemsTrade.member.domain.MemberRequestDto;
import com.project.usedItemsTrade.member.domain.MemberResponseDto;
import com.project.usedItemsTrade.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    @ApiOperation(value = "일반회원가입", notes = "소셜로그인이 아닌 일반 회원가입입니다")
    public ResponseEntity<?> join(@Valid @RequestBody MemberRequestDto.MemberJoinDto joinDto) {
        memberService.join(joinDto);

        return ResponseEntity.ok().body(true);
    }

    @GetMapping("/authEmail")
    @ApiOperation(value = "회원가입 이메일 인증", notes = "이메일 인증을 진행합니다")
    public ResponseEntity<?> authenticateEmail(@Valid @ModelAttribute("emailDto")MemberRequestDto.EmailDto emailDto) {
        memberService.emailAuth(emailDto);

        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/pwdResetMail")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "비밀번호 초기화 코드 발송", notes = "비밀번호 초기화 코드를 메일로 발송합니다")
    public ResponseEntity<?> sendPasswordResetMail(@Valid @RequestBody MemberRequestDto.EmailDto emailDto) {
        memberService.sendPasswordResetMail(emailDto.getEmail());

        return ResponseEntity.ok().body(true);
    }

    @GetMapping("/info")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "회원정보 조회", notes = "회원정보를 조회합니다")
    public ResponseEntity<MemberResponseDto.MemberInfoDto> getMyInformation(Principal principal) {
        MemberResponseDto.MemberInfoDto memberInfoDto =
                memberService.myInfo(principal.getName());

        return ResponseEntity.ok().body(memberInfoDto);
    }

    @PostMapping("/updateInfo")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "회원정보 수정", notes = "회원정보를 업데이트 합니다")
    public ResponseEntity<?> updateMyInformation(@Valid @RequestBody MemberRequestDto.MemberUpdateInfoDto updateInfoDto) {
        memberService.updateMyInfo(updateInfoDto);

        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/resetPwd")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "비밀번호 초기화", notes = "비밀번호 초기화 코드를 받고 초기화합니다")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody MemberRequestDto.ResetPasswordDto resetPasswordDto) {
        memberService.resetPassword(resetPasswordDto);

        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "회원탈퇴", notes = "회원상태를 탈퇴로 변경하고 개인정보를 삭제후 탈퇴를 진행합니다")
    public ResponseEntity<?> withdrawMember(@Valid @RequestBody MemberRequestDto.MemberWithdrawDto withdrawDto) {
        memberService.withdraw(withdrawDto);

        return ResponseEntity.ok().body(true);
    }
}
