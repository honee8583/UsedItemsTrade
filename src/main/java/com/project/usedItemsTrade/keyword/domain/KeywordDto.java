package com.project.usedItemsTrade.keyword.domain;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KeywordDto {
    private Long id;
    private String keywordName;

    public static KeywordDto entityToDto(Keyword keyword) {
        return KeywordDto.builder()
                .id(keyword.getId())
                .keywordName(keyword.getKeywordName())
                .build();
    }
}
