package com.project.usedItemsTrade.board.repository;

import com.project.usedItemsTrade.board.domain.Board;
import com.project.usedItemsTrade.board.domain.Image;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class ImageRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ImageRepository imageRepository;

    @Test
    @DisplayName("deleteByBoard() 쿼리 메소드 테스트")
    void testDeleteByBoard() {
        // given
        Board board = Board.builder()
                .build();

        Image image = Image.builder()
                .board(board)
                .build();

        entityManager.persist(board);
        entityManager.persist(image);
        entityManager.flush();

        // when
        imageRepository.deleteByBoard(board);
        Optional<Image> optionalImage = imageRepository.findById(1L);

        // then
        assertFalse(optionalImage.isPresent());
    }

    @Test
    @DisplayName("findAllByBoard() 쿼리 메소드 테스트")
    void testFindAllByBoard() {
        // given
        Board board = Board.builder()
                .build();

        Image image1 = Image.builder()
                .board(board)
                .build();

        Image image2 = Image.builder()
                .board(board)
                .build();

        entityManager.persist(board);
        entityManager.persist(image1);
        entityManager.persist(image2);
        entityManager.flush();

        // when
        List<Image> imageList = imageRepository.findAllByBoard(board);

        // then
        assertEquals(imageList.size(), 2);
    }
}