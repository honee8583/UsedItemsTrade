package com.project.usedItemsTrade.reply.service.impl;

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
import com.project.usedItemsTrade.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void write(ReplyRequestDto.ReplyRegisterDto registerDto) {
        Member member = memberRepository.findByEmail(registerDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다!"));

        Board board = boardRepository.findById(registerDto.getBoardId())
                .orElseThrow(NoBoardExistsException::new);

        Reply reply = Reply.builder()
                .content(registerDto.getContent())
                .member(member)
                .board(board)
                .build();

        replyRepository.save(reply);
    }

    @Override
    @Transactional
    public void update(ReplyRequestDto.ReplyUpdateDto updateDto) {
        Reply reply = replyRepository.findById(updateDto.getId())
                .orElseThrow(NoReplyExistsException::new);

        Member member = memberRepository.findByEmail(updateDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다!"));

        if (!reply.getMember().getEmail().equals(updateDto.getEmail())) {
            throw new UserNotMatchException();
        }

        reply.updateReply(updateDto);

        replyRepository.save(reply);
    }

    @Override
    @Transactional
    public void remove(Long id, String email) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(NoReplyExistsException::new);

        if (!reply.getMember().getEmail().equals(email)) {
            throw new UserNotMatchException();
        }

        replyRepository.delete(reply);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReplyDto> getReplyList(ReplyRequestDto.ReplySearchDto searchDto) {
        Board board = boardRepository.findById(searchDto.getBoardId())
                .orElseThrow(NoBoardExistsException::new);

        List<Reply> replyList = replyRepository.findByBoardId(board.getId());

        return replyList.stream().map(ReplyDto::entityToDto).collect(Collectors.toList());
    }
}
