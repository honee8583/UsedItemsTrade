package com.project.usedItemsTrade.board.service.impl;

import com.project.usedItemsTrade.board.domain.*;
import com.project.usedItemsTrade.board.error.NoBoardExistsException;
import com.project.usedItemsTrade.board.error.UserNotMatchException;
import com.project.usedItemsTrade.board.repository.BoardRepository;
import com.project.usedItemsTrade.board.service.BoardService;
import com.project.usedItemsTrade.member.domain.Member;
import com.project.usedItemsTrade.member.repository.MemberRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void register(BoardRequestDto.BoardRegisterDto registerDto, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("작성자가 존재하지 않습니다!"));

        Board board = Board.dtoToBoard(registerDto, member.getEmail());

        boardRepository.save(board);
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDto get(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(NoBoardExistsException::new);

        return BoardDto.entityToDto(board);
    }

    @Override
    @Transactional
    public void updateBoard(BoardRequestDto.BoardUpdateDto updateDto, String email) {
        Board board = boardRepository.findById(updateDto.getId())
                .orElseThrow(NoBoardExistsException::new);

        if (!board.getMember().getEmail().equals(email)) {
            throw new UserNotMatchException();
        }

        board.update(updateDto);

        boardRepository.save(board);
    }

    @Override
    @Transactional
    public void deleteBoard(Long id, String email) {
        Board board = boardRepository.findById(id)
                .orElseThrow(NoBoardExistsException::new);

        if (!board.getMember().getEmail().equals(email)) {
            throw new UserNotMatchException();
        }

        boardRepository.delete(board);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardDto> searchBoard(BoardRequestDto.BoardSearchDto searchDto) {
        BooleanBuilder booleanBuilder = getSearchBooleanBuilder(searchDto);

        // 정렬조건
        List<Sort.Order> orders = new ArrayList<>();
        if (searchDto.isPriceOrder()) {
            orders.add(new Sort.Order(Sort.Direction.DESC, "price"));
        }
        if (searchDto.isViewOrder()) {
            orders.add(new Sort.Order(Sort.Direction.DESC, "view"));
        }
        if (searchDto.isCreatedAtOrder()) {
            orders.add(new Sort.Order(Sort.Direction.DESC, "createdAt"));
        }

        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize(), Sort.by(orders));

        Page<Board> pageList = boardRepository.findAll(booleanBuilder, pageable);

        return pageList.stream()
                        .map(BoardDto::entityToDto)
                        .collect(Collectors.toList());
    }

    // 키워드 검색 booleanBuilder 생성
    private BooleanBuilder getSearchBooleanBuilder(BoardRequestDto.BoardSearchDto searchDto) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QBoard qBoard = QBoard.board;

        String type = searchDto.getType();
        String keyword = searchDto.getKeyword();

        if (type.contains("T")) {
            booleanBuilder.or(qBoard.title.contains(keyword));
        }

        if (type.contains("C")) {
            booleanBuilder.or(qBoard.content.contains(keyword));
        }

        if (type.contains("B")) {
            booleanBuilder.and(qBoard.boardStatus.eq(BoardStatus.SELL));
        }

        return booleanBuilder;
    }
}
