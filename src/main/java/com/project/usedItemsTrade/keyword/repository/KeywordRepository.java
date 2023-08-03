package com.project.usedItemsTrade.keyword.repository;

import com.project.usedItemsTrade.keyword.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByKeywordName(String keywordName);
    void deleteByKeywordName(String keywordName);
}
