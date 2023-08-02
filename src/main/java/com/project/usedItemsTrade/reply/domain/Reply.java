package com.project.usedItemsTrade.reply.domain;

import com.project.usedItemsTrade.board.domain.Board;
import com.project.usedItemsTrade.member.domain.BaseEntity;
import com.project.usedItemsTrade.member.domain.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String content;

    public void updateReply(ReplyRequestDto.ReplyUpdateDto updateDto) {
        this.content = updateDto.getContent();
    }
}
