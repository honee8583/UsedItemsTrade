package com.project.usedItemsTrade.board.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private int price;
    private BoardStatus boardStatus;
    private int view;
    private String email;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static BoardDto entityToDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .price(board.getPrice())
                .boardStatus(board.getBoardStatus())
                .view(board.getView())
                .email(board.getMember().getEmail())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }
}
