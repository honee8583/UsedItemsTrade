package com.project.usedItemsTrade.keyword.service;

import com.project.usedItemsTrade.keyword.error.KeywordAlreadyExistsException;
import com.project.usedItemsTrade.keyword.error.KeywordNotExistsException;
import com.project.usedItemsTrade.keyword.domain.Keyword;
import com.project.usedItemsTrade.keyword.domain.KeywordDto;
import com.project.usedItemsTrade.keyword.domain.KeywordRequestDto;
import com.project.usedItemsTrade.keyword.repository.KeywordRepository;
import com.project.usedItemsTrade.keyword.service.impl.KeywordServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class KeywordServiceTest {

    @Mock
    private KeywordRepository keywordRepository;

    @InjectMocks
    private KeywordServiceImpl keywordService;

    private Keyword createKeyword() {
        return Keyword.builder()
                .id(1L)
                .keywordName("electronic")
                .comment("This is electronic keyword")
                .build();
    }

    @Test
    @DisplayName("키워드 생성 테스트")
    void testCreateKeyword() {
        // given
        KeywordRequestDto.KeywordCreateDto createDto = KeywordRequestDto.KeywordCreateDto
                .builder()
                .keywordName("electronic")
                .comment("This is Electronic Device keyword")
                .build();

        Keyword keyword = Keyword
                .builder()
                .keywordName("electronic")
                .comment("This is Electronic Device keyword")
                .build();

        given(keywordRepository.findByKeywordName(anyString())).willReturn(Optional.empty());

        // when
        keywordService.createKeyword(createDto);

        // then
        verify(keywordRepository, times(1))
                .save(any(Keyword.class));
        ArgumentCaptor<Keyword> keywordArgumentCaptor = ArgumentCaptor.forClass(Keyword.class);
        verify(keywordRepository).save(keywordArgumentCaptor.capture());

        Keyword savedKeyword = keywordArgumentCaptor.getValue();
        assertEquals(savedKeyword.getKeywordName(), createDto.getKeywordName());
        assertEquals(savedKeyword.getComment(), createDto.getComment());
    }

    @Test
    @DisplayName("키워드 생성시 키워드가 이미 존재할경우 예외 발생 테스트")
    void testCreateKeyword_KeywordAlreadyExistsException() {
        // given
        Keyword savedKeyword = Keyword.builder()
                .keywordName("electronic")
                .comment("This is electronic keyword")
                .build();

        KeywordRequestDto.KeywordCreateDto createDto = KeywordRequestDto.KeywordCreateDto
                        .builder()
                .keywordName("electronic")
                .comment("This is electronic keyword!")
                        .build();

        given(keywordRepository.findByKeywordName(anyString()))
                .willReturn(Optional.of(savedKeyword));

        // when
        // then
        assertThrows(KeywordAlreadyExistsException.class,
                () -> keywordService.createKeyword(createDto));
    }

    @Test
    @DisplayName("키워드 수정 테스트")
    void testUpdateKeyword() {
        // given
        Keyword keyword = createKeyword();

        KeywordRequestDto.KeywordUpdateDto updateDto = KeywordRequestDto.KeywordUpdateDto
                        .builder()
                        .id(1L)
                        .keywordName("electronic Keyword")
                        .comment("This is electronic keyword!!!")
                        .build();

        given(keywordRepository.findById(anyLong()))
                .willReturn(Optional.of(keyword));

        // when
        keywordService.updateKeyword(updateDto);

        // then
        verify(keywordRepository, times(1))
                .save(any(Keyword.class));
        ArgumentCaptor<Keyword> keywordArgumentCaptor =
                ArgumentCaptor.forClass(Keyword.class);
        verify(keywordRepository).save(keywordArgumentCaptor.capture());

        Keyword savedKeyword = keywordArgumentCaptor.getValue();
        assertEquals(savedKeyword.getComment(), updateDto.getComment());
        assertEquals(savedKeyword.getKeywordName(), updateDto.getKeywordName());
    }

    @Test
    @DisplayName("키워드 수정시 존재하지 않는 키워드일 경우 예외 발생 테스트")
    void testUpdateKeyword_KeywordNotExistsException() {
        // given
        KeywordRequestDto.KeywordUpdateDto updateDto = KeywordRequestDto.KeywordUpdateDto
                .builder()
                .id(1L)
                .keywordName("electronic Keyword")
                .comment("This is electronic keyword!!!")
                .build();

        // when
        when(keywordRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThrows(KeywordNotExistsException.class,
                () -> keywordService.updateKeyword(updateDto));
    }

    @Test
    @DisplayName("키워드 삭제 테스트")
    void testDeleteKeyword() {
        // given
        Keyword savedKeyword = createKeyword();

        given(keywordRepository.findById(anyLong())).willReturn(Optional.of(savedKeyword));

        // when
        keywordService.deleteKeyword(1L);

        // then
        verify(keywordRepository, times(1)).delete(any(Keyword.class));
    }

    @Test
    @DisplayName("키워드 삭제시 존재하지 않는 키워드일경우 예외 발생 테스트")
    void testDeleteKeyword_KeywordNotExistsException() {
        // given

        // when
        when(keywordRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThrows(KeywordNotExistsException.class,
                () -> keywordService.deleteKeyword(1L));
    }

    @Test
    @DisplayName("키워드 목록 불러오기 테스트")
    void testGetKeywordList() {
        // given
        List<Keyword> keywordList = Arrays.asList(
                Keyword.builder().keywordName("keyword1")
                        .comment("keyword1").build(),
                Keyword.builder().keywordName("keyword2")
                        .comment("keyword2").build()
        );

        KeywordRequestDto.KeywordPageDto pageDto = KeywordRequestDto.KeywordPageDto
                                            .builder()
                                            .page(0)
                                            .build();


        Page<Keyword> pageKeywordList = new PageImpl<>(keywordList);

        given(keywordRepository.findAll(any(Pageable.class))).willReturn(pageKeywordList);

        // when
        List<KeywordDto> keywordDtoList = keywordService.getKeywordList(pageDto);

        // then
        assertEquals(keywordDtoList.size(), 2);
    }
}