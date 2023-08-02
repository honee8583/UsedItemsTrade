package com.project.usedItemsTrade.board.domain;

import com.project.usedItemsTrade.member.domain.BaseEntity;
import com.project.usedItemsTrade.member.domain.Member;
import com.project.usedItemsTrade.reply.domain.Reply;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private int price;

    @Enumerated(EnumType.STRING)
    private BoardStatus boardStatus;

//    private String keyword;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    private int view;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    // TODO 조회수 증가

    public void update(BoardRequestDto.BoardUpdateDto updateDto) {
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
        this.price = updateDto.getPrice();
        this.boardStatus = updateDto.getBoardStatus();
    }

    public static Board dtoToBoard(BoardRequestDto.BoardRegisterDto registerDto, String email) {
        return Board.builder()
                .title(registerDto.getTitle())
                .content(registerDto.getContent())
                .price(registerDto.getPrice())
                .boardStatus(registerDto.getBoardStatus())
                .member(Member.builder().email(email).build())
                .build();
    }
}


