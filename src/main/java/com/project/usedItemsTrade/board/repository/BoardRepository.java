package com.project.usedItemsTrade.board.repository;

import com.project.usedItemsTrade.board.domain.Board;
import com.project.usedItemsTrade.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, QuerydslPredicateExecutor<Board> {
    void deleteAllByMember(Member member);
    List<Board> findAllByMember(Member member);
}
