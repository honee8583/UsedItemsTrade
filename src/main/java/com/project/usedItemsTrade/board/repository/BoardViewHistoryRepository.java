package com.project.usedItemsTrade.board.repository;

import com.project.usedItemsTrade.board.domain.Board;
import com.project.usedItemsTrade.board.domain.BoardViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardViewHistoryRepository extends JpaRepository<BoardViewHistory, Long> {
    Optional<BoardViewHistory> findByBoardAndUserEmail(Board board, String email);
}
