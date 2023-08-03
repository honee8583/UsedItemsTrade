package com.project.usedItemsTrade.board.domain;

import com.project.usedItemsTrade.keyword.domain.Keyword;
import com.project.usedItemsTrade.member.domain.BaseEntity;
import com.project.usedItemsTrade.member.domain.Member;
import com.project.usedItemsTrade.reply.domain.Reply;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private int view;

    @Enumerated(EnumType.STRING)
    private BoardStatus boardStatus;

    @OneToMany
    private List<Keyword> keywordList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replyList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void increaseView() {
        this.view++;
    }

    public void update(BoardRequestDto.BoardUpdateDto updateDto) {
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
        this.price = updateDto.getPrice();
        this.boardStatus = updateDto.getBoardStatus();
    }

    public static Board dtoToBoard(BoardRequestDto.BoardRegisterDto registerDto, String email) {
        List<Keyword> keywordList = new ArrayList<>();
        if (registerDto.getKeywordIds() != null) {
            keywordList = registerDto.getKeywordIds()
                    .stream()
                    .map(id -> Keyword.builder().id(id).build())
                    .collect(Collectors.toList());
        }

        return Board.builder()
                .title(registerDto.getTitle())
                .content(registerDto.getContent())
                .price(registerDto.getPrice())
                .boardStatus(registerDto.getBoardStatus())
                .member(Member.builder().email(email).build())
                .keywordList(keywordList)
                .build();
    }
}


