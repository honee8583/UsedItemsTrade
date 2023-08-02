package com.project.usedItemsTrade.reply.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.usedItemsTrade.reply.domain.ReplyDto;
import com.project.usedItemsTrade.reply.domain.ReplyRequestDto;
import com.project.usedItemsTrade.reply.service.impl.ReplyServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReplyServiceImpl replyService;

    private final String jwt = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGVtYWlsLmNvbSIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY5MDk0MTM1NCwiZXhwIjoxNjkwOTQ0OTU0fQ.k9zc14Gr5ESkY7F-RaCGVH2nBnPoigBdbLwvEAt-jSZgW3iiHdvIfK3Az2BjWk1YF-HlZGHpZZRJNe4WbAcJKw";

    @Test
    @DisplayName("writeReply() 컨트롤러 메소드 테스트")
    void testWriteReply() throws Exception {
        // given
        ReplyRequestDto.ReplyRegisterDto registerDto = ReplyRequestDto.ReplyRegisterDto
                        .builder()
                        .email("user@email.com")
                        .boardId(1L)
                        .content("content")
                        .build();

        // when
        doNothing().when(replyService).write(registerDto);

        // then
        mockMvc.perform(post("/reply/write")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(registerDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("updateReply() 컨트롤러 메소드 테스트")
    void testUpdate() throws Exception {
        // given
        ReplyRequestDto.ReplyUpdateDto updateDto = ReplyRequestDto.ReplyUpdateDto
                .builder()
                .id(1L)
                .content("modified reply")
                .email("user@email.com")
                .build();

        // when
        replyService.update(updateDto);

        // then
        mockMvc.perform(put("/reply/update")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("deleteReply() 컨트롤러 메소드 테스트")
    void testDeleteReply() throws Exception {
        // given
        Long replyId = 1L;
        String email = "user@email.com";

        // when
        replyService.remove(replyId, email);

        // then
        mockMvc.perform(delete("/reply/delete")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(replyId)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("getReplyList() 컨트롤러 메소드 테스트")
    void testGetReplyList() throws Exception {
        // given
        ReplyRequestDto.ReplySearchDto searchDto = ReplyRequestDto.ReplySearchDto
                .builder()
                .boardId(1L)
                .build();

        // when
        List<ReplyDto> replyDtoList = replyService.getReplyList(searchDto);

        // then
        mockMvc.perform(get("/reply/list")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(searchDto)))
                .andExpect(status().isOk());
    }
}