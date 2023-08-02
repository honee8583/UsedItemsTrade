package com.project.usedItemsTrade.reply.service;

import com.project.usedItemsTrade.board.domain.Board;
import com.project.usedItemsTrade.board.error.NoBoardExistsException;
import com.project.usedItemsTrade.board.error.UserNotMatchException;
import com.project.usedItemsTrade.board.repository.BoardRepository;
import com.project.usedItemsTrade.member.domain.Member;
import com.project.usedItemsTrade.member.repository.MemberRepository;
import com.project.usedItemsTrade.reply.domain.Reply;
import com.project.usedItemsTrade.reply.domain.ReplyDto;
import com.project.usedItemsTrade.reply.domain.ReplyRequestDto;
import com.project.usedItemsTrade.reply.error.NoReplyExistsException;
import com.project.usedItemsTrade.reply.repository.ReplyRepository;
import com.project.usedItemsTrade.reply.service.impl.ReplyServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ReplyServiceTest {

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ReplyServiceImpl replyService;

    private final Long boardId = 1L;
    private final Long replyId = 1L;
    private final String email = "user@email.com";

    private Member createMember() {
        return Member.builder().email(email).build();
    }

    private Board createBoard() {
        return Board.builder().id(boardId).build();
    }

    private Reply createReply() {
        return Reply.builder()
                .id(replyId)
                .board(createBoard())
                .member(createMember())
                .build();
    }

    private ReplyRequestDto.ReplyRegisterDto createRegisterDto() {
        return ReplyRequestDto.ReplyRegisterDto
                .builder()
                .email(email)
                .boardId(boardId)
                .content("content")
                .build();
    }

    private ReplyRequestDto.ReplyUpdateDto createUpdateDto() {
        return ReplyRequestDto.ReplyUpdateDto
                .builder()
                .id(1L)
                .email("user@email.com")
                .content("updated reply")
                .build();
    }

    private ReplyRequestDto.ReplySearchDto createSearchDto() {
        return ReplyRequestDto.ReplySearchDto
                .builder()
                .boardId(1L)
                .build();
    }

    @Test
    @DisplayName("댓글 작성 테스트")
    void testWrite() {
        // given
        Member member = createMember();

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(member));

        Board board = createBoard();

        given(boardRepository.findById(anyLong()))
                .willReturn(Optional.of(board));

        ReplyRequestDto.ReplyRegisterDto registerDto = createRegisterDto();

        // when
        replyService.write(registerDto);

        // then
        verify(replyRepository, times(1)).save(any(Reply.class));
        ArgumentCaptor<Reply> replyArgumentCaptor = ArgumentCaptor.forClass(Reply.class);
        verify(replyRepository).save(replyArgumentCaptor.capture());

        Reply reply = replyArgumentCaptor.getValue();
        assertEquals(1L, reply.getBoard().getId());
        assertEquals("user@email.com", reply.getMember().getEmail());
        assertEquals("content", reply.getContent());
    }

    @Test
    @DisplayName("댓글 작성시 작성자가 존재하지 않을 경우 예외 발생 테스트")
    void testWrite_UsernameNotFoundException() {
        // given
        ReplyRequestDto.ReplyRegisterDto registerDto = createRegisterDto();

        // when
        when(memberRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        // then
        assertThrows(UsernameNotFoundException.class, () -> replyService.write(registerDto));
    }

    @Test
    @DisplayName("댓글 작성시 댓글이 속해있는 게시판이 존재하지 않을 경우 예외 발생 테스트")
    void testWrite_NoBoardExistsException() {
        // given
        Member member = createMember();

        ReplyRequestDto.ReplyRegisterDto registerDto = createRegisterDto();

        // when
        when(memberRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(member));

        when(boardRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // then
        assertThrows(NoBoardExistsException.class, () -> replyService.write(registerDto));
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void testUpdate() {
        // given
        ReplyRequestDto.ReplyUpdateDto updateDto = createUpdateDto();

        Member member = createMember();

        Reply reply = createReply();

        // when
        when(replyRepository.findById(anyLong()))
                .thenReturn(Optional.of(reply));

        when(memberRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(member));

        replyService.update(updateDto);

        // then
        verify(replyRepository, times(1)).save(any(Reply.class));
        ArgumentCaptor<Reply> replyArgumentCaptor = ArgumentCaptor.forClass(Reply.class);
        verify(replyRepository).save(replyArgumentCaptor.capture());

        Reply capturedReply = replyArgumentCaptor.getValue();
        assertEquals("user@email.com", capturedReply.getMember().getEmail());
        assertEquals("updated reply", capturedReply.getContent());
    }

    @Test
    @DisplayName("댓글 수정시 댓글이 존재하지 않을 경우 예외 발생 테스트")
    void testUpdate_NoReplyExistsException() {
        // given
        ReplyRequestDto.ReplyUpdateDto updateDto = createUpdateDto();

        // when
        when(replyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(NoReplyExistsException.class, () -> replyService.update(updateDto));
    }

    @Test
    @DisplayName("댓글 수정시 댓글 작성자가 존재하지 않을 경우 예외 발생 테스트")
    void testUpdate_UsernameNotFoundException() {
        // given
        Reply reply = createReply();

        ReplyRequestDto.ReplyUpdateDto updateDto = createUpdateDto();

        given(replyRepository.findById(anyLong())).willReturn(Optional.of(reply));

        // when
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // then
        assertThrows(UsernameNotFoundException.class, () -> replyService.update(updateDto));
    }

    @Test
    @DisplayName("댓글 수정시 본인의 댓글이 아닐경우 예외 발생 테스트")
    void testUpdate_UserNotMatchException() {
        // given
        ReplyRequestDto.ReplyUpdateDto updateDto = createUpdateDto();
        updateDto.setEmail("user2@email.com");

        Reply reply = createReply();

        // when
        given(replyRepository.findById(anyLong()))
                .willReturn(Optional.of(reply));

        given(memberRepository.findByEmail(anyString()))
                .willReturn(Optional.of(reply.getMember()));

        // then
        assertThrows(UserNotMatchException.class, () -> replyService.update(updateDto));
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void testRemove() {
        // given
        Reply reply = createReply();

        // when
        when(replyRepository.findById(anyLong())).thenReturn(Optional.of(reply));

        replyService.remove(replyId, email);

        // then
        verify(replyRepository, times(1)).delete(any(Reply.class));
    }

    @Test
    @DisplayName("댓글 삭제시 댓글이 이미 존재하지 않을 경우 예외 발생 테스트")
    void testRemove_NoReplyExistsException() {
        // given

        // when
        when(replyRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(NoReplyExistsException.class, () -> replyService.remove(replyId, email));
    }

    @Test
    @DisplayName("댓글 삭제시 댓글 작성자와 현재 로그인한 사용자가 같지 않을 경우 예외 발생 테스트")
    void testRemove_UserNotMatchException() {
        // given
        Reply reply = createReply();

        // when
        when(replyRepository.findById(anyLong())).thenReturn(Optional.of(reply));

        // then
        assertThrows(UserNotMatchException.class, () -> replyService.remove(1L, "user2@email.com"));
    }

    @Test
    @DisplayName("게시물의 댓글목록 불러오기 테스트")
    void testGetReplyList() {
        // given
        ReplyRequestDto.ReplySearchDto searchDto = createSearchDto();

        Board board = createBoard();

        List<Reply> replyList = Arrays.asList(
                Reply.builder().id(1L).board(board)
                        .member(Member.builder().email("user@email.com").build())
                        .build(),
                Reply.builder().id(2L).board(board)
                        .member(Member.builder().email("user@email.com").build())
                        .build()
        );

        given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
        given(replyRepository.findByBoardId(anyLong())).willReturn(replyList);

        // when
        List<ReplyDto> replyDtoList = replyService.getReplyList(searchDto);

        // then
        assertEquals(replyDtoList.size(), 2);
        assertEquals(replyDtoList.get(0).getId(), 1L);
        assertEquals(replyDtoList.get(1).getId(), 2L);
    }

    @Test
    @DisplayName("댓글목록 불러올 때 게시물이 존재하지 않을 경우 예외 발생 테스트")
    void testGetReplyList_NoBoardExistsException() {
        // given
        ReplyRequestDto.ReplySearchDto searchDto = createSearchDto();

        // when
        when(boardRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(NoBoardExistsException.class,
                () -> replyService.getReplyList(searchDto));
    }
}