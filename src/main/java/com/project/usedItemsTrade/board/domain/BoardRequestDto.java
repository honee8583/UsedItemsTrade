package com.project.usedItemsTrade.board.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class BoardRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class BoardRegisterDto {
        private String title;
        private String content;
        private int price;
        private BoardStatus boardStatus;
        private List<String> keywordList = new ArrayList<>();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class BoardUpdateDto {
        private Long id;
        private String title;
        private String content;
        private int price;
        private BoardStatus boardStatus;

        @Builder.Default
        private List<String> updateKeywordList = new ArrayList<>();     // 수정 키워드 리스트

        @Builder.Default
        private List<ImageDto.ImageUploadDto> deleteImageList = new ArrayList<>();  // 삭제 이미지
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class BoardSearchDto {
        private int page = 0;
        private final int size = 10;
        private String type;    // Title, Content
        private String keyword;
        private boolean priceOrder;
        private boolean viewOrder;
        private boolean createdAtOrder;
        // TODO BoardStatus가 Sell인 물건 검색
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardDeleteDto {
        private Long boardId;

        @Builder.Default
        private List<ImageDto.UploadResultDto> deleteImageList = new ArrayList<>();
    }
}
