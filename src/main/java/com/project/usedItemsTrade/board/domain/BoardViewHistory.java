package com.project.usedItemsTrade.board.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BoardViewHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String userEmail;

    @Column(name = "view_time")
    private LocalDateTime viewTime;

    public void updateViewTime() {
        this.viewTime = LocalDateTime.now();
    }
}
