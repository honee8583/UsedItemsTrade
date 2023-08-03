package com.project.usedItemsTrade.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.usedItemsTrade.board.domain.BoardDto;
import com.project.usedItemsTrade.board.domain.BoardRequestDto;
import com.project.usedItemsTrade.board.domain.BoardStatus;
import com.project.usedItemsTrade.board.service.impl.BoardServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardServiceImpl boardService;

    private static final String jwt = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGVtYWlsLmNvbSIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY5MDk1MTg2MiwiZXhwIjoxNjkwOTU1NDYyfQ.f6kdSrOL7xdxna96dq0Am5c77MJcS3a6ebZEkip00NAdtGXiyxaT7xrS7CIvDaSv2VKPzZ4i852ThLvfoBI6iw";

    @Test
    @DisplayName("registerBoard 컨트롤러 메소드 테스트")
    void testRegisterBoard() throws Exception {
        // given
        BoardRequestDto.BoardRegisterDto registerDto = BoardRequestDto.BoardRegisterDto
                .builder()
                .title("title")
                .content("content")
                .boardStatus(BoardStatus.SELL)
                .price(1000)
                .build();

        // when
        doNothing().when(boardService).register(registerDto, "user@email.com");

        // then
        mockMvc.perform(post("/board/register")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(registerDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("get 컨트롤러 메소드 테스트")
    void testGet() throws Exception {
        // given
        Long id = 1L;
        BoardDto boardDto = BoardDto.builder()
                        .title("title")
                        .content("content")
                        .email("user@email.com")
                        .price(1000)
                        .boardStatus(BoardStatus.SELL)
                        .build();

        // when
        when(boardService.get(anyLong(), anyString())).thenReturn(boardDto);

        // then
        mockMvc.perform(get("/board/get/1")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.email").value("user@email.com"))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.boardStatus").value(BoardStatus.SELL.name()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("updateBoard 컨트롤러 메소드 테스트")
    void testUpdateBoard() throws Exception {
        // given
        BoardRequestDto.BoardUpdateDto updateDto = BoardRequestDto.BoardUpdateDto
                .builder()
                .id(1L)
                .title("title")
                .content("content")
                .boardStatus(BoardStatus.SELL)
                .build();

        // when
        doNothing().when(boardService).updateBoard(updateDto, "test@email.com");

        // then
        mockMvc.perform(put("/board/update")
                .header("Authorization", jwt)
                .content(new ObjectMapper().writeValueAsString(updateDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("deleteBoard 컨트롤러 메소드 테스트")
    void testDeleteBoard() throws Exception {
        // given
        Long id = 1L;

        // when
        doNothing().when(boardService).deleteBoard(id, "user@email.com");

        // then
        mockMvc.perform(delete("/board/delete?id=1")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("list 컨트롤러 메소드 테스트")
    void testList() throws Exception {
        // given
        BoardRequestDto.BoardSearchDto searchDto = BoardRequestDto.BoardSearchDto
                .builder()
                .type("tc")
                .keyword("title")
                .page(0)
                .priceOrder(true)
                .build();

        BoardDto boardDto1 = BoardDto.builder()
                .id(1L)
                .title("title1")
                .content("content1")
                .email("user@email.com")
                .price(1000)
                .view(10)
                .boardStatus(BoardStatus.SELL)
                .build();

        BoardDto boardDto2 = BoardDto.builder()
                .id(2L)
                .title("title2")
                .content("content2")
                .email("user2@email.com")
                .price(1000)
                .view(10)
                .boardStatus(BoardStatus.SELL)
                .build();

        List<BoardDto> boardDtoList = Arrays.asList(boardDto1, boardDto2);

        // when
        when(boardService.searchBoard(any())).thenReturn(boardDtoList);

        // then
        mockMvc.perform(get("/board/list?type=TC%keyword=title&page=0&priceOrder=true")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}