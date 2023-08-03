package com.project.usedItemsTrade.keyword.service;

import com.project.usedItemsTrade.keyword.domain.KeywordDto;
import com.project.usedItemsTrade.keyword.domain.KeywordRequestDto;

import java.util.List;

public interface KeywordService {
    // 키워드 생성
    void createKeyword(KeywordRequestDto.KeywordCreateDto createDto);
    // 키워드 수정
    void updateKeyword(KeywordRequestDto.KeywordUpdateDto updateDto);
    // 키워드 삭제
    void deleteKeyword(Long id);
    // 키워드 조회
    List<KeywordDto> getKeywordList(KeywordRequestDto.KeywordPageDto keywordPageDto);
}
