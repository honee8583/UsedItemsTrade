package com.project.usedItemsTrade.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.usedItemsTrade.member.domain.MemberRequestDto;
import com.project.usedItemsTrade.member.domain.MemberResponseDto;
import com.project.usedItemsTrade.member.domain.MemberStatus;
import com.project.usedItemsTrade.member.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberServiceImpl memberService;

    private static final String jwt = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGVtYWlsLmNvbSIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY5MTExMTMyOSwiZXhwIjoxNjkxMTE0OTI5fQ.dlLv1YqXW_6uEZpXCMVN6rBSLQIxrIUz44sIFF02vuMj_K7UAhaz7xaNTaP9Ygdr-1UQLLtLO0jFeAMZ527xVA";

    @Test
    @DisplayName("POST /member/join 테스트")
    void testJoin() throws Exception {
        // given
        MemberRequestDto.MemberJoinDto joinDto = MemberRequestDto.MemberJoinDto
                .builder()
                .email("user@email.com")
                .password("1111")
                .name("user")
                .phone("010-3333-3333")
                .build();

        // when
        doNothing().when(memberService).join(any());

        // then
        mockMvc.perform(post("/member/join")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(joinDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /member/pwdResetMail 테스트")
    void testPwdResetMail() throws Exception {
        // given
        MemberRequestDto.EmailDto emailDto = MemberRequestDto.EmailDto
                .builder()
                .email("user@email.com")
                .emailAuthCode("")
                .build();

        // when
        doNothing().when(memberService).sendPasswordResetMail(emailDto.getEmail());

        // then
        mockMvc.perform(post("/member/pwdResetMail")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(emailDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /member/info 테스트")
    void testInfo() throws Exception {
        // given
        MemberResponseDto.MemberInfoDto memberInfoDto = MemberResponseDto.MemberInfoDto
                .builder()
                .email("user@email.com")
                .name("user")
                .phone("010-1111-1111")
                .status(MemberStatus.AVAILABLE)
                .build();

        // when
        when(memberService.myInfo(anyString())).thenReturn(memberInfoDto);

        // then
        mockMvc.perform(get("/member/info?email=user@email.com")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@email.com"));
    }

    @Test
    @DisplayName("POST /member/updateInfo 테스트")
    void testUpdateInfo() throws Exception {
        // given
        MemberRequestDto.MemberUpdateInfoDto updateInfoDto =
                MemberRequestDto.MemberUpdateInfoDto
                        .builder()
                        .email("user@email.com")
                        .name("user")
                        .phone("010-2222-2222")
                        .build();

        // when
        doNothing().when(memberService).updateMyInfo(any());

        // then
        mockMvc.perform(post("/member/updateInfo")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateInfoDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /member/authEmail 테스트")
    void testAuthEmail() throws Exception {
        // given
        MemberRequestDto.EmailDto emailDto = MemberRequestDto.EmailDto
                .builder()
                .email("user@email.com")
                .emailAuthCode("1111")
                .build();

        // when
        doNothing().when(memberService).emailAuth(emailDto);

        // then
        mockMvc.perform(post("/member/authEmail")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(emailDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /member/resetPwd 테스트")
    void testResetPwd() throws Exception {
        // given
        MemberRequestDto.ResetPasswordDto resetPasswordDto = MemberRequestDto.ResetPasswordDto
                .builder()
                .newPwd("newPassword")
                .resetPasswordKey("resetPasswordKey")
                .build();

        // when
        doNothing().when(memberService).resetPassword(any());

        // then
        mockMvc.perform(post("/member/resetPwd")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(resetPasswordDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /member/withdraw 테스트")
    void testWithDraw() throws Exception {
        // given
        MemberRequestDto.MemberWithdrawDto withdrawDto = MemberRequestDto.MemberWithdrawDto
                .builder()
                .email("user@email.com")
                .password("1111")
                .build();

        // when
        doNothing().when(memberService).withdraw(any());

        // then
        mockMvc.perform(post("/member/withdraw")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(withdrawDto)))
                .andExpect(status().isOk());

    }
}