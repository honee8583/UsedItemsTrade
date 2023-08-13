package com.project.usedItemsTrade.board.controller;

import com.project.usedItemsTrade.board.domain.ImageDto;
import com.project.usedItemsTrade.board.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<List<ImageDto.UploadResultDto>> uploadFile(MultipartFile[] files) {
        List<ImageDto.UploadResultDto> uploadResultDtoList = imageService.uploadImages(files);

        return ResponseEntity.ok().body(uploadResultDtoList);   // 업로드 결과를 브라우저에 반환
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {

        return imageService.getFile(fileName);
    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName) {
        boolean result = imageService.removeFile(fileName);

        return ResponseEntity.ok().body(result);
    }
}
