package com.project.usedItemsTrade.keyword.domain;

import lombok.*;

public class KeywordRequestDto {

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeywordCreateDto {
        private String keywordName;
        private String comment;
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeywordUpdateDto {
        private Long id;
        private String keywordName;
        private String comment;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class KeywordPageDto {
        private int page;
    }
}
