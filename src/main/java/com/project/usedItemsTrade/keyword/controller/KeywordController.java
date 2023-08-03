package com.project.usedItemsTrade.keyword.controller;

import com.project.usedItemsTrade.keyword.domain.KeywordDto;
import com.project.usedItemsTrade.keyword.domain.KeywordRequestDto;
import com.project.usedItemsTrade.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/keyword")
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createKeyword(@RequestBody KeywordRequestDto.KeywordCreateDto createDto) {
        keywordService.createKeyword(createDto);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateKeyword(@RequestBody KeywordRequestDto.KeywordUpdateDto updateDto) {
        keywordService.updateKeyword(updateDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteKeyword(@RequestParam Long id) {
        keywordService.deleteKeyword(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<KeywordDto>> getKeywordList(KeywordRequestDto.KeywordPageDto pageDto) {
        List<KeywordDto> keywordDtoList = keywordService.getKeywordList(pageDto);

        return ResponseEntity.ok().body(keywordDtoList);
    }
}
