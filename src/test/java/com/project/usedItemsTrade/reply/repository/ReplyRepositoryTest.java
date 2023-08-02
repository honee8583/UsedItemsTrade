package com.project.usedItemsTrade.reply.repository;

import com.project.usedItemsTrade.board.domain.Board;
import com.project.usedItemsTrade.reply.domain.Reply;
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
class ReplyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    void testFindByBoardId() {
        // given
        Board board = Board.builder()
                .title("title")
                .content("content")
                .build();

        Reply reply1 = Reply.builder()
                .board(Board.builder().id(1L).build())
                .content("reply1")
                .build();

        Reply reply2 = Reply.builder()
                .board(Board.builder().id(1L).build())
                .content("reply2")
                .build();

        entityManager.persist(board);
        entityManager.flush();

        entityManager.persist(reply1);
        entityManager.persist(reply2);
        entityManager.flush();

        // when
        List<Reply> replyList = replyRepository.findByBoardId(board.getId());

        // then
        assertEquals(2, replyList.size());

    }
}