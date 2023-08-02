package com.project.usedItemsTrade.reply.controller;

import com.project.usedItemsTrade.reply.domain.ReplyDto;
import com.project.usedItemsTrade.reply.domain.ReplyRequestDto;
import com.project.usedItemsTrade.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;

    // 댓글작성
    @PostMapping("/write")
    public ResponseEntity<Void> writeReply(@RequestBody ReplyRequestDto.ReplyRegisterDto registerDto, Principal principal) {
        registerDto.setEmail(principal.getName());
        replyService.write(registerDto);

        return ResponseEntity.ok().build();
    }

    // 댓글수정
    @PutMapping("/update")
    public ResponseEntity<Void> updateReply(@RequestBody ReplyRequestDto.ReplyUpdateDto updateDto, Principal principal) {
        updateDto.setEmail(principal.getName());
        replyService.update(updateDto);

        return ResponseEntity.ok().build();
    }

    // 댓글삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteReply(@RequestParam("id") long id, Principal principal) {
        replyService.remove(id, principal.getName());

        return ResponseEntity.ok().build();
    }

    // 게시판의 댓글 목록
    @GetMapping("/list")
    public ResponseEntity<List<ReplyDto>> getReplyList(ReplyRequestDto.ReplySearchDto searchDto) {
        List<ReplyDto> replyDtoList = replyService.getReplyList(searchDto);

        return ResponseEntity.ok().body(replyDtoList);
    }
}
