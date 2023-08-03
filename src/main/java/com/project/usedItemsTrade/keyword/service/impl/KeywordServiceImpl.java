package com.project.usedItemsTrade.keyword.service.impl;

import com.project.usedItemsTrade.keyword.error.KeywordAlreadyExistsException;
import com.project.usedItemsTrade.keyword.error.KeywordNotExistsException;
import com.project.usedItemsTrade.keyword.domain.Keyword;
import com.project.usedItemsTrade.keyword.domain.KeywordDto;
import com.project.usedItemsTrade.keyword.domain.KeywordRequestDto;
import com.project.usedItemsTrade.keyword.repository.KeywordRepository;
import com.project.usedItemsTrade.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {
    private final KeywordRepository keywordRepository;

    @Override
    @Transactional
    public void createKeyword(KeywordRequestDto.KeywordCreateDto createDto) {
        Optional<Keyword> optionalKeyword =
                keywordRepository.findByKeywordName(createDto.getKeywordName());

        if (optionalKeyword.isPresent()) {
            throw new KeywordAlreadyExistsException();
        }

        Keyword keyword = Keyword.builder()
                .keywordName(createDto.getKeywordName())
                .comment(createDto.getComment())
                .build();

        keywordRepository.save(keyword);
    }

    @Override
    @Transactional
    public void updateKeyword(KeywordRequestDto.KeywordUpdateDto updateDto) {
        Keyword keyword = keywordRepository.findById(updateDto.getId())
                .orElseThrow(KeywordNotExistsException::new);

        keyword.updateKeyword(updateDto);

        keywordRepository.save(keyword);
    }

    @Override
    @Transactional
    public void deleteKeyword(Long id) {
        Keyword keyword = keywordRepository.findById(id)
                        .orElseThrow(KeywordNotExistsException::new);

        keywordRepository.delete(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KeywordDto> getKeywordList(KeywordRequestDto.KeywordPageDto keywordPageDto) {
        Pageable pageable = PageRequest.of(keywordPageDto.getPage(), 10,
                Sort.by("createdDate").descending());

        Page<Keyword> keywordList = keywordRepository.findAll(pageable);

        return keywordList.stream()
                .map(KeywordDto::entityToDto)
                .collect(Collectors.toList());
    }
}
