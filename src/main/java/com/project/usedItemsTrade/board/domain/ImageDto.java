package com.project.usedItemsTrade.board.domain;

import com.project.usedItemsTrade.board.error.GetImageUrlFailedException;
import lombok.*;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ImageDto {

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageUploadDto {
        private String fileName;    // 업로드된 파일의 원래 이름
        private String uuid;        // 파일의 UUID 값
        private String folderPath;  // 업로드된 파일의 저장 경로

        public String getImageURL() {
            try {
                return URLEncoder.encode(folderPath + "/" + uuid + "_" + fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new GetImageUrlFailedException();
            }
        }
    }

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadResultDto implements Serializable{    // 브라우저에서 필요한 정보
        private Long id;
        private String fileName;    // 업로드된 파일의 원래 이름
        private String uuid;        // 파일의 UUID 값
        private String folderPath;  // 업로드된 파일의 저장 경로

        // 파일의 전체경로가 필요할 경우
        public String getImageURL() {
            try {
                return URLEncoder.encode(folderPath + "/" + uuid + "_" + fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new GetImageUrlFailedException();
            }
        }

        public static UploadResultDto entityToDto(Image image) {
            return UploadResultDto.builder()
                    .id(image.getId())
                    .fileName(image.getFileName())
                    .uuid(image.getUuid())
                    .folderPath(image.getFolderPath())
                    .build();
        }
    }
}
