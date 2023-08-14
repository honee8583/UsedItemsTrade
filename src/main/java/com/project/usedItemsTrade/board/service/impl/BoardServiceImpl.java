package com.project.usedItemsTrade.board.service.impl;

import com.project.usedItemsTrade.board.domain.*;
import com.project.usedItemsTrade.board.error.NoBoardExistsException;
import com.project.usedItemsTrade.board.error.UserNotMatchException;
import com.project.usedItemsTrade.board.repository.BoardRepository;
import com.project.usedItemsTrade.board.repository.BoardViewHistoryRepository;
import com.project.usedItemsTrade.board.repository.ImageRepository;
import com.project.usedItemsTrade.board.service.BoardService;
import com.project.usedItemsTrade.board.service.ImageService;
import com.project.usedItemsTrade.keyword.error.KeywordNotExistsException;
import com.project.usedItemsTrade.keyword.repository.KeywordRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final ImageService imageService;
    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final KeywordRepository keywordRepository;
    private final BoardViewHistoryRepository boardViewHistoryRepository;

    public void register(BoardRequestDto.BoardRegisterDto registerDto, MultipartFile[] images, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다!"));

        // 받아온 키워드 리스트가 있을 경우 존재하는 키워드인지 검사
        if (registerDto.getKeywordList() != null && registerDto.getKeywordList().size() > 0) {
            for(String keywordName : registerDto.getKeywordList()) {
                keywordRepository.findByKeywordName(keywordName)
                        .orElseThrow(KeywordNotExistsException::new);
            }
        }

        // 게시글 저장
        Board board = Board.dtoToBoard(registerDto, member.getEmail());
        boardRepository.save(board);

        // 이미지 저장
        if (images != null && images.length > 0) {
            List<ImageDto.UploadResultDto> uploadResultDtoList = imageService.uploadImages(images);  // 이미지 업로드

            for (ImageDto.UploadResultDto uploadResultDto : uploadResultDtoList) {
                imageRepository.save(Image.dtoToEntity(uploadResultDto, board));
            }
        }
    }

    @Override
    @Transactional
    public BoardDto get(Long id, String email) {
        Board board = boardRepository.findById(id)
                .orElseThrow(NoBoardExistsException::new);

        Optional<BoardViewHistory> viewHistory =
                boardViewHistoryRepository.findByBoardAndUserEmail(board, email);

        // 아직 방문한적이 없다면
        if (viewHistory.isEmpty()) {
            BoardViewHistory history = BoardViewHistory.builder()
                    .board(board)
                    .userEmail(email)
                    .viewTime(LocalDateTime.now())
                    .build();

            boardViewHistoryRepository.save(history);
        } else {
            // 방문한적이 있을 경우
            BoardViewHistory history = viewHistory.get();
            if (LocalDateTime.now().isAfter(history.getViewTime().plusDays(1))) {
                // 조회수 증가
                board.increaseView();
                history.updateViewTime();

                boardViewHistoryRepository.save(history);
                boardRepository.save(board);

                log.info(board.getId() + "번 게시물의 조회수를 증가시킵니다");
            }
        }

        List<Image> imageList = imageRepository.findAllByBoard(board);
        List<ImageDto.UploadResultDto> uploadResultDtoList = imageList
                .stream()
                .map(ImageDto.UploadResultDto::entityToDto)
                .collect(Collectors.toList());

        return BoardDto.entityToDtoWithImageDto(board, uploadResultDtoList);
    }

    @Override
    @Transactional
    public void updateBoard(BoardRequestDto.BoardUpdateDto updateDto, MultipartFile[] images, String email) {
        Board board = boardRepository.findById(updateDto.getId())
                .orElseThrow(NoBoardExistsException::new);

        if (!board.getMember().getEmail().equals(email)) {
            throw new UserNotMatchException();
        }

        board.update(updateDto);
        boardRepository.save(board);

        // 업로드된 이미지파일들 삭제
        updateDto.getDeleteImageList().forEach((image)
                -> imageService.removeFile(image.getImageURL()));

        // 기존 이미지 정보 데이터베이스 삭제
        imageRepository.deleteByBoard(board);

        if (images != null && images.length > 0) {
            // 새 이미지 파일 업로드
            List<ImageDto.UploadResultDto> uploadResultDtoList = imageService.uploadImages(images);

            // 새 이미지 정보 데이터베이스 저장
            for(ImageDto.UploadResultDto uploadResultDto : uploadResultDtoList) {
                imageRepository.save(Image.dtoToEntity(uploadResultDto, board));
            }
        }
    }

    @Override
    @Transactional
    public void deleteBoard(BoardRequestDto.BoardDeleteDto deleteDto, String email) {
        Board board = boardRepository.findById(deleteDto.getBoardId())
                .orElseThrow(NoBoardExistsException::new);

        if (!board.getMember().getEmail().equals(email)) {
            throw new UserNotMatchException();
        }

        // 업로드된 이미지 삭제
        if (deleteDto.getDeleteImageList() != null && deleteDto.getDeleteImageList().size() > 0) {
            for (ImageDto.UploadResultDto resultDto : deleteDto.getDeleteImageList()) {
                imageService.removeFile(resultDto.getImageURL());
            }
            imageRepository.deleteByBoard(board);
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
