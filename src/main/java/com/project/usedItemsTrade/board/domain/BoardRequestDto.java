package com.project.usedItemsTrade.board.domain;

import com.project.usedItemsTrade.keyword.domain.KeywordDto;
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
        private List<Long> keywordIds = new ArrayList<>();
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
//        private String order;   // createdAt, price, view
        private boolean priceOrder;
        private boolean viewOrder;
        private boolean createdAtOrder;
        // TODO BoardStatus가 Sell인 물건 검색
    }


}
