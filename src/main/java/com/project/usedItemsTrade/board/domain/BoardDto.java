package com.project.usedItemsTrade.board.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardDto {
    private Long id;
    private String email;
    private String title;
    private String content;
    private int price;
    private int view;
    private BoardStatus boardStatus;

    private List<String> keywordList;
    private List<ImageDto.UploadResultDto> imageDtoList;

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
                .keywordList(board.getKeywordList())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }

    public static BoardDto entityToDtoWithImageDto(Board board, List<ImageDto.UploadResultDto> imageList) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .price(board.getPrice())
                .boardStatus(board.getBoardStatus())
                .view(board.getView())
                .email(board.getMember().getEmail())
                .keywordList(board.getKeywordList())
                .imageDtoList(imageList)
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }
}
