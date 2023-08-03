package com.project.usedItemsTrade.board.repository;

import com.project.usedItemsTrade.board.domain.Board;
import com.project.usedItemsTrade.board.domain.BoardViewHistory;
import com.project.usedItemsTrade.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class BoardViewHistoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BoardViewHistoryRepository viewHistoryRepository;

    @Test
    @DisplayName("findByBoardAndUserEmail() 쿼리 메소드 테스트")
    void testFindByBoardAndUserEmail() {
        // given
        String userEmail = "user@email.com";
        Board board = Board.builder()
                .build();

        BoardViewHistory history = BoardViewHistory.builder()
                .board(board)
                .userEmail(userEmail)
                .viewTime(LocalDateTime.now())
                .build();

        entityManager.persist(board);
        entityManager.persist(history);
        entityManager.flush();

        // when
        Optional<BoardViewHistory> viewHistory =
                viewHistoryRepository.findByBoardAndUserEmail(board, userEmail);

        // then
        BoardViewHistory savedHistory = viewHistory.get();
        assertEquals(savedHistory.getId(), 1L);
        assertEquals(savedHistory.getUserEmail(), userEmail);
        assertEquals(savedHistory.getBoard().getId(), 1L);
    }
}