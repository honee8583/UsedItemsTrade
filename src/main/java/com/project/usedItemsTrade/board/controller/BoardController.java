package com.project.usedItemsTrade.board.controller;

import com.project.usedItemsTrade.board.domain.BoardDto;
import com.project.usedItemsTrade.board.domain.BoardRequestDto;
import com.project.usedItemsTrade.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> registerBoard(BoardRequestDto.BoardRegisterDto registerDto, MultipartFile[] images, Principal principal) throws IOException {
        boardService.register(registerDto, images, principal.getName());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable("id") Long id, Principal principal) {
        BoardDto boardDto = boardService.get(id, principal.getName());

        return ResponseEntity.ok().body(boardDto);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateBoard(BoardRequestDto.BoardUpdateDto updateDto, MultipartFile[] images, Principal principal) {
        boardService.updateBoard(updateDto, images, principal.getName());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteBoard(@RequestBody BoardRequestDto.BoardDeleteDto deleteDto, Principal principal) {
        boardService.deleteBoard(deleteDto, principal.getName());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<BoardDto>> getBoardList(BoardRequestDto.BoardSearchDto searchDto) {
        List<BoardDto> boardList = boardService.searchBoard(searchDto);

        return ResponseEntity.ok().body(boardList);
    }
}
