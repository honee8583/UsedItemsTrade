package com.project.usedItemsTrade.board.service;

import com.project.usedItemsTrade.board.domain.BoardDto;
import com.project.usedItemsTrade.board.domain.BoardRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BoardService {
    // 게시물(판매글/구매글) 작성
    void register(BoardRequestDto.BoardRegisterDto registerDto, MultipartFile[] images, String email);
    // 게시물 조회(+ 조회수 증가)
    BoardDto get(Long id, String email);
    // 게시물 수정 상태변환(판매중/거래중/거래완료)
    void updateBoard(BoardRequestDto.BoardUpdateDto updateDto, MultipartFile[] images, String email);
    // 게시글 삭제
    void deleteBoard(Long id, String email);
    // 게시물 검색(제목, 내용, 가격정렬)
    List<BoardDto> searchBoard(BoardRequestDto.BoardSearchDto searchDto);
    // TODO 키워드를 이용한 검색
}
