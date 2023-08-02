package com.project.usedItemsTrade.reply.domain;

import lombok.*;

public class ReplyRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ReplyRegisterDto {
        private Long boardId;
        private String content;
        private String email;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ReplyUpdateDto {
        private Long id;
        private String email;
        private String content;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ReplySearchDto {
        private Long boardId;
    }
}
