package com.project.usedItemsTrade.board.service.impl;

import com.project.usedItemsTrade.board.domain.ImageDto;
import com.project.usedItemsTrade.board.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Value("${spring.file.upload.path}")
    private String uploadPath;

    @Override
    public List<ImageDto.UploadResultDto> uploadImages(MultipartFile[] images) {
        List<ImageDto.UploadResultDto> resultDtoList = new ArrayList<>();
        for (MultipartFile file : images) {
            if(!file.getContentType().startsWith("image")) {
                log.warn("this is not image type");
                throw new RuntimeException("이미지 파일은 업로드가 불가능합니다!");     // TODO HttpStatus.FORBIDDEN 리턴(CustomException)
            }

            String originalName = file.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("업로드 파일명: " + fileName);

            //폴더 구분
            String folderPath = makeFolder();
            String uuid = UUID.randomUUID().toString();

            //파일명 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);
            try {
                file.transferTo(savePath);
                resultDtoList.add(new ImageDto.UploadResultDto(null, fileName, uuid, folderPath));
            } catch (IOException e) {
                throw new RuntimeException("파일을 저장하는데 실패하였습니다!");   // TODO CustomException
            }
        }

        return resultDtoList;
    }

    // 인코딩된 파일 이름을 파라미터로 받아서 해당 파일을 byte[]로 만들어서 브라우저로 전송
    @Override
    public byte[] getFile(String fileName) {
        try{
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("fileName: " + srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);
            log.info("file: " + file);

            HttpHeaders header = new HttpHeaders();
            header.add("Content-Type", Files.probeContentType(file.toPath()));  // 파일확장자에 따른 MIME타입

            return FileCopyUtils.copyToByteArray(file);
        } catch(Exception e) {
            throw new RuntimeException("이미지를 불러오는데 실패하였습니다!");  // TODO CustomException
        }
    }

    @Override
    public Boolean removeFile(String fileName) {
        String srcFileName = null;
        try{
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);

            return file.delete();
        } catch(UnsupportedEncodingException e) {
            throw new RuntimeException("파일 삭제에 실패하였습니다!");  // TODO CustomException(파일삭제)
        }
    }

    private String makeFolder(){
        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("//", File.separator);

        File uploadPathFolder = new File(uploadPath, folderPath);

        if(!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }
}
