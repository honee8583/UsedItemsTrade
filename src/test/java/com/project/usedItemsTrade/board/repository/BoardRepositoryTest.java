package com.project.usedItemsTrade.board.repository;

import com.project.usedItemsTrade.board.domain.Board;
import com.project.usedItemsTrade.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class BoardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BoardRepository boardRepository;

    private Member createMember() {
        return Member.builder()
                .email("user@email.com")
                .password("1111")
                .name("user")
                .build();
    }

    @Test
    @DisplayName("deleteAllByMember() 쿼리메소드 테스트")
    void testDeleteAllByMember() {
        // given
        Member member = createMember();

        Board board1 = Board.builder()
                        .member(member)
                        .build();

        Board board2 = Board.builder()
                        .member(member)
                        .build();

        entityManager.persist(member);
        entityManager.persist(board1);
        entityManager.persist(board2);
        entityManager.flush();

        // when
        boardRepository.deleteAllByMember(member);

        // then
        List<Board> boardList = boardRepository.findAllByMember(member);
        assertEquals(boardList.size(), 0);
    }

    @Test
    @DisplayName("findAllByMember() 쿼리메소드 테스트")
    void testFindAllByMember() {
        // given
        Member member = createMember();

        Board board1 = Board.builder()
                .member(member)
                .build();

        Board board2 = Board.builder()
                .member(member)
                .build();

        entityManager.persist(member);
        entityManager.persist(board1);
        entityManager.persist(board2);
        entityManager.flush();

        // when
        List<Board> boardList = boardRepository.findAllByMember(member);

        // then
        assertEquals(boardList.size(), 2);
    }
}