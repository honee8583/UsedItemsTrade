package com.project.usedItemsTrade.board.service;

import com.project.usedItemsTrade.board.domain.*;
import com.project.usedItemsTrade.board.error.NoBoardExistsException;
import com.project.usedItemsTrade.board.error.UserNotMatchException;
import com.project.usedItemsTrade.board.repository.BoardRepository;
import com.project.usedItemsTrade.board.repository.BoardViewHistoryRepository;
import com.project.usedItemsTrade.board.repository.ImageRepository;
import com.project.usedItemsTrade.board.service.impl.BoardServiceImpl;
import com.project.usedItemsTrade.board.service.impl.ImageServiceImpl;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    private ImageRepository imageRepository;

    @Mock
    private ImageServiceImpl imageService;

    @Mock
    private BoardViewHistoryRepository viewHistoryRepository;

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
    void testRegister() throws IOException {
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
        boardService.register(registerDto, null,"user@email.com");

        // then
        verify(boardRepository, times(1)).save(any(Board.class));
        ArgumentCaptor<Board> boardArgumentCaptor = ArgumentCaptor.forClass(Board.class);
        verify(boardRepository).save(boardArgumentCaptor.capture());

        Board board = boardArgumentCaptor.getValue();
        assertEquals(board.getMember().getEmail(), "user@email.com");
        assertEquals(board.getTitle(), "title");
    }

    @Test
    @DisplayName("Board 업로드시 존재하지 않는 작성자일 경우 예외 발생 테스트")
    void testRegister_UsernameNotFoundException() {
        // given
        BoardRequestDto.BoardRegisterDto registerDto =
                BoardRequestDto.BoardRegisterDto
                        .builder()
                        .build();

        // when
        when(memberRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        // then
        assertThrows(UsernameNotFoundException.class,
                () -> boardService.register(registerDto,null,"user@email.com"));
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

        BoardViewHistory viewHistory = BoardViewHistory.builder()
                .id(1L)
                .board(board)
                .userEmail("user@email.com")
                .viewTime(LocalDateTime.now())
                .build();

        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        given(viewHistoryRepository.findByBoardAndUserEmail(board, "user@email.com"))
                .willReturn(Optional.of(viewHistory));

        // when
        BoardDto boardDto = boardService.get(id, "user@email.com");

        // then
        assertEquals(id, boardDto.getId());
        assertEquals("title", boardDto.getTitle());
        assertEquals("content", boardDto.getContent());
        assertEquals("user@email.com", boardDto.getEmail());
        assertEquals(BoardStatus.SELL, boardDto.getBoardStatus());
        assertEquals(1000, boardDto.getPrice());
    }

    @Test
    @DisplayName("Board 조회시 존재하지 않는 글일 경우 예외 발생 테스트")
    void testGet_NoBoardExistsException() {
        // given
        Long id = 1L;
        String email = "user@email.com";

        // when
        when(boardRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThrows(NoBoardExistsException.class,
                () -> boardService.get(id, email));
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
        boardService.updateBoard(updateDto, null, "user@email.com");

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

    @Test
    @DisplayName("Board 수정시 수정할 글이 존재하지 않을 경우 예외 발생 테스트")
    void testUpdateBoard_NoBoardExistsException() {
        // given
        BoardRequestDto.BoardUpdateDto updateDto = BoardRequestDto.BoardUpdateDto
                .builder()
                .build();

        // when
        when(boardRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThrows(NoBoardExistsException.class,
                () -> boardService.updateBoard(updateDto, null, "user@email.com"));
    }

    @Test
    @DisplayName("Board 수정시 작성자 정보가 일치하지 않을 경우 예외 발생 테스트")
    void testUpdateBoard_UserNotMatchException() {
        // given
        Board board = Board.builder()
                .id(1L)
                .member(Member.builder().email("user@email.com").build())
                .build();

        BoardRequestDto.BoardUpdateDto updateDto =
                BoardRequestDto.BoardUpdateDto
                                .builder()
                                .id(1L)
                                .build();

        // when
        when(boardRepository.findById(anyLong()))
                .thenReturn(Optional.of(board));

        // then
        assertThrows(UserNotMatchException.class,
                () -> boardService.updateBoard(updateDto, null, "user2@email.com"));

    }

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
        verify(imageRepository, times(1)).deleteByBoard(any(Board.class));
    }

    @Test
    @DisplayName("Board 삭제시 존재하지 않는 게시글일 경우 예외 발생 테스트")
    void testDeleteBoard_NoBoardExistsException() {
        // given
        Long id = 1L;
        String email = "user@email.com";

        // when
        when(boardRepository.findById(id))
                .thenReturn(Optional.empty());

        // then
        assertThrows(NoBoardExistsException.class,
                () -> boardService.deleteBoard(id, email));
    }

    @Test
    void testDeleteBoard_UserNotMatchException() {
        // given
        Long id = 1L;
        String email = "user2@email.com";

        Board board = Board.builder()
                .member(Member.builder().email("user@email.com").build())
                .build();

        // when
        when(boardRepository.findById(anyLong()))
                .thenReturn(Optional.of(board));

        // then
        assertThrows(UserNotMatchException.class,
                () -> boardService.deleteBoard(id, email));
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
}