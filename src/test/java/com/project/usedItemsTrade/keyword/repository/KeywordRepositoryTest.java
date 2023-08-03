package com.project.usedItemsTrade.keyword.repository;

import com.project.usedItemsTrade.keyword.domain.Keyword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class KeywordRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private KeywordRepository keywordRepository;

    @Test
    @DisplayName("findByKeywordName() 쿼리 메소드 테스트")
    void testFindByKeywordName() {
        // given
        String keywordName = "electronic";

        Keyword keyword = Keyword.builder().keywordName(keywordName).build();

        entityManager.persist(keyword);
        entityManager.flush();

        // when
        Optional<Keyword> optionalKeyword = keywordRepository.findByKeywordName(keywordName);
        Keyword savedKeyword = optionalKeyword.get();

        // then
        assertEquals(savedKeyword.getKeywordName(), keywordName);
    }

    @Test
    @DisplayName("deleteByKeywordName() 쿼리 메소드 테스트")
    void testDeleteByKeywordName() {
        // given
        String keywordName = "electronic";
        Keyword keyword = Keyword.builder().keywordName(keywordName).build();

        entityManager.persist(keyword);
        entityManager.flush();

        // when
        keywordRepository.deleteByKeywordName(keywordName);
        Optional<Keyword> optionalKeyword = keywordRepository.findByKeywordName(keywordName);

        // then
        assertFalse(optionalKeyword.isPresent());
    }
}