package com.project.usedItemsTrade.board.service;

import com.project.usedItemsTrade.board.domain.*;
import com.project.usedItemsTrade.board.repository.BoardRepository;
import com.project.usedItemsTrade.board.service.impl.BoardServiceImpl;
import com.project.usedItemsTrade.member.domain.Member;
import com.project.usedItemsTrade.member.repository.MemberRepository;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private BoardServiceImpl boardService;

    private List<Board> getBoardList() {
        List<Board> boards = new ArrayList<>();
        boards.add(Board.builder()
                .title("title1")
                .content("content1")
                .price(1000)
                .member(Member.builder().email("user@email.com").build())
                .boardStatus(BoardStatus.SELL)
                .view(10)
                .build());

        boards.add(Board.builder()
                .title("title2")
                .content("content2")
                .price(10000)
                .member(Member.builder().email("user2@email.com").build())
                .boardStatus(BoardStatus.SELL)
                .view(8)
                .build());

        boards.add(Board.builder()
                .title("title3")
                .content("content3")
                .price(500)
                .member(Member.builder().email("user3@email.com").build())
                .boardStatus(BoardStatus.SELL)
                .view(20)
                .build());

        return boards;
    }

    @Test
    @DisplayName("Board 업로드 테스트")
    void testRegister() {
        // given
        BoardRequestDto.BoardRegisterDto registerDto = BoardRequestDto.BoardRegisterDto
                .builder()
                .title("title")
                .content("content")
                .price(1000)
                .boardStatus(BoardStatus.SELL)
                .build();

        Member member = Member.builder()
                .email("user@email.com")
                .build();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));

        // when
        boardService.register(registerDto, "user@email.com");

        // then
        verify(boardRepository, times(1)).save(any(Board.class));
        ArgumentCaptor<Board> boardArgumentCaptor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(boardArgumentCaptor.capture());

        Board board = boardArgumentCaptor.getValue();
        assertEquals(board.getMember().getEmail(), "user@email.com");
        assertEquals(board.getTitle(), "title");
    }

    @Test
    @DisplayName("Board 조회 테스트")
    void testGet() {
        // given
        Long id = 1L;
        Board board = Board.builder()
                .id(1L)
                .title("title")
                .content("content")
                .member(Member.builder().email("user@email.com").build())
                .boardStatus(BoardStatus.SELL)
                .price(1000)
                .build();

        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // when
        BoardDto boardDto = boardService.get(id);

        // then
        assertEquals(id, boardDto.getId());
        assertEquals("title", boardDto.getTitle());
        assertEquals("content", boardDto.getContent());
        assertEquals("user@email.com", boardDto.getEmail());
        assertEquals(BoardStatus.SELL, boardDto.getBoardStatus());
        assertEquals(1000, boardDto.getPrice());
    }

    @Test
    @DisplayName("Board 수정 테스트")
    void testUpdateBoard() {
        // given
        BoardRequestDto.BoardUpdateDto updateDto = BoardRequestDto.BoardUpdateDto
                .builder()
                .id(1L)
                .title("updatedTitle")
                .content("updatedContent")
                .price(10000)
                .boardStatus(BoardStatus.TRADE_COMPLETE)    // 거래완료
                .build();

        Board board = Board.builder()
                .id(1L)
                .title(updateDto.getTitle())
                .content(updateDto.getContent())
                .member(Member.builder().email("user@email.com").build())
                .price(updateDto.getPrice())
                .boardStatus(updateDto.getBoardStatus())
                .build();

//        given(principal.getName()).willReturn("user@email.com");

        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

        // when
        boardService.updateBoard(updateDto, "user@email.com");

        // then
        verify(boardRepository, times(1)).save(any(Board.class));
        ArgumentCaptor<Board> boardArgumentCaptor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(boardArgumentCaptor.capture());

        Board updatedBoard = boardArgumentCaptor.getValue();
        assertEquals(updateDto.getTitle(), updatedBoard.getTitle());
        assertEquals(updateDto.getContent(), updatedBoard.getContent());
        assertEquals(updateDto.getPrice(), updatedBoard.getPrice());
        assertEquals(updateDto.getBoardStatus(), updatedBoard.getBoardStatus());
    }

    // TODO 댓글 삭제 cascade 테스트

    @Test
    @DisplayName("Board 삭제 테스트")
    void testDeleteBoard() {
        // given
        Long id = 1L;
        Board board = Board.builder()
                .id(1L)
                .member(Member.builder().email("user@email.com").build())
                .build();

        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(board));

        // when
        boardService.deleteBoard(id, "user@email.com");

        // then
        verify(boardRepository, times(1)).delete(any(Board.class));
    }

    @Test
    @DisplayName("Board 검색 테스트")
    void testSearchBoard() {
        // given
        BoardRequestDto.BoardSearchDto searchDto = BoardRequestDto.BoardSearchDto
                .builder()
                .keyword("title")
                .type("T")  // 제목에 title을 포함한 게시물
                .priceOrder(true) // price 내림차순 정렬
                .build();

        Page<Board> pageList = new PageImpl<>(getBoardList());

        // when
        when(boardRepository.findAll(any(BooleanBuilder.class), any(Pageable.class)))
                .thenReturn(pageList);

        List<BoardDto> result = boardService.searchBoard(searchDto);

        // then
        assertEquals(3, result.size());
    }

    // TODO 예외 발생 테스트
}