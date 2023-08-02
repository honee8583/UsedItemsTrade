package com.project.usedItemsTrade.reply.repository;

import com.project.usedItemsTrade.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByBoardId(Long boardId);
}
