package com.project.usedItemsTrade.board.service;

import com.project.usedItemsTrade.board.domain.ImageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    // 파일 업로드
    List<ImageDto.UploadResultDto> uploadImages(MultipartFile[] images);
    // 파일 조회
    ResponseEntity<byte[]> getFile(String fileName);

    Boolean removeFile(String fileName);
}
