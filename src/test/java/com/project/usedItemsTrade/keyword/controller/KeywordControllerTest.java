package com.project.usedItemsTrade.keyword.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.usedItemsTrade.keyword.domain.KeywordDto;
import com.project.usedItemsTrade.keyword.domain.KeywordRequestDto;
import com.project.usedItemsTrade.keyword.service.impl.KeywordServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class KeywordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KeywordServiceImpl keywordService;

    private static final String jwt = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGVtYWlsLmNvbSIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX0FETUlOIn1dLCJpYXQiOjE2OTEwMjU4MjQsImV4cCI6MTY5MTAyOTQyNH0.KLqFBavuYzZlo7b-nqCWUH4Es3uOOgATnkwAQjbiiTrzld-h6EehJXtweMSXRqtzwZuvMHRlEWAjPZHa-XWovA";

    @Test
    @DisplayName("키워드 생성 컨트롤러 메소드 테스트")
    void testCreateKeyword() throws Exception {
        // given
        KeywordRequestDto.KeywordCreateDto createDto =
                KeywordRequestDto.KeywordCreateDto.builder()
                        .keywordName("keyword test")
                        .comment("this is keyword test")
                        .build();

        // when
        doNothing().when(keywordService).createKeyword(createDto);

        // then
        mockMvc.perform(post("/keyword/create")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("키워드 수정 컨트롤러 메소드 테스트")
    void testUpdateKeyword() throws Exception {
        // given
        KeywordRequestDto.KeywordUpdateDto updateDto = KeywordRequestDto.KeywordUpdateDto
                .builder()
                .keywordName("keyword")
                .comment("keyword test")
                .build();

        // when
        doNothing().when(keywordService).updateKeyword(updateDto);

        // then
        mockMvc.perform(put("/keyword/update")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("키워드 삭제 컨트롤러 메소드 테스트")
    void testDeleteKeyword() throws Exception {
        // given
        Long id = 1L;

        // when
        doNothing().when(keywordService).deleteKeyword(id);

        // then
        mockMvc.perform(delete("/keyword/delete")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", String.valueOf(id)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("키워드 리스트 컨트롤러 메소드 테스트")
    void testGetKeywordList() throws Exception {
        // given
        KeywordRequestDto.KeywordPageDto pageDto =
                KeywordRequestDto.KeywordPageDto.builder()
                        .page(0)
                        .build();

        List<KeywordDto> keywordDtoList = Arrays.asList(
                KeywordDto.builder().keywordName("keyword").build(),
                KeywordDto.builder().keywordName("keyword2").build()
        );

        // when
        when(keywordService.getKeywordList(any())).thenReturn(keywordDtoList);

        // then
        mockMvc.perform(get("/keyword/list")
                .header("Authorization", jwt)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}