package com.dino.plExplorer.repository;

import com.dino.plExplorer.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    boolean existsByEspnId(Long espnId);

    List<Article> findTop5ByOrderByDateCreatedDesc();
}
