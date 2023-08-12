package com.project.usedItemsTrade.board.repository;

import com.project.usedItemsTrade.board.domain.Board;
import com.project.usedItemsTrade.board.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteByBoard(Board board);
    List<Image> findAllByBoard(Board board);
}
