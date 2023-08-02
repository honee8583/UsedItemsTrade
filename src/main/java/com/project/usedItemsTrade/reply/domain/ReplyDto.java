package com.project.usedItemsTrade.reply.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {
    private Long id;
    private Long boardId;
    private String email;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static ReplyDto entityToDto(Reply reply) {
        return ReplyDto.builder()
                .id(reply.getId())
                .boardId(reply.getBoard().getId())
                .email(reply.getMember().getEmail())
                .content(reply.getContent())
                .createdDate(reply.getCreatedDate())
                .modifiedDate(reply.getModifiedDate())
                .build();
    }
}
