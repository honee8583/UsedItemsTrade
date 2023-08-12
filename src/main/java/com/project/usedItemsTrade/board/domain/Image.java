package com.project.usedItemsTrade.board.domain;

import com.project.usedItemsTrade.member.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity
@ToString(exclude = "board")
@NoArgsConstructor
@AllArgsConstructor
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String folderPath;
    private String uuid;
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    public static Image dtoToEntity(ImageDto.UploadResultDto resultDto, Board board) {
        return Image.builder()
                .uuid(resultDto.getUuid())
                .folderPath(resultDto.getFolderPath())
                .fileName(resultDto.getFileName())
                .board(board)
                .build();
    }
}
