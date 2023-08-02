package com.project.usedItemsTrade.reply.service;

import com.project.usedItemsTrade.reply.domain.ReplyDto;
import com.project.usedItemsTrade.reply.domain.ReplyRequestDto;

import java.util.List;

public interface ReplyService {
    // 댓글작성
    void write(ReplyRequestDto.ReplyRegisterDto registerDto);
    // 댓글수정
    void update(ReplyRequestDto.ReplyUpdateDto updateDto);
    // 댓글삭제
    void remove(Long id, String email);
    // 댓글 리스트 조회
    List<ReplyDto> getReplyList(ReplyRequestDto.ReplySearchDto searchDto);
}
