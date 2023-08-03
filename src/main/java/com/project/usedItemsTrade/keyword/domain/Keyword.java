package com.project.usedItemsTrade.keyword.domain;

import com.project.usedItemsTrade.member.domain.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Keyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keywordName;

    private String comment;

    public void updateKeyword(KeywordRequestDto.KeywordUpdateDto updateDto) {
        this.keywordName = updateDto.getKeywordName();
        this.comment = updateDto.getComment();
    }
}
