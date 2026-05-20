package com.dino.plExplorer.scheduler;

import com.dino.plExplorer.dto.external.espn.news.EspnNewsDto;
import com.dino.plExplorer.repository.ArticleRepository;
import com.dino.plExplorer.service.articles.ArticlesService;
import com.dino.plExplorer.service.espn.EspnApiService;
import com.dino.plExplorer.service.gemini.GeminiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsScheduler {
    private final GeminiService geminiService;
    private final EspnApiService espnApiService;
    private final ArticlesService articlesService;
    private final ArticleRepository articleRepository;

    @Scheduled(initialDelay = 0, fixedRate = 2 * 60 * 60 * 1000)
    public void fetchAndProcessNews() {
        Optional<EspnNewsDto> response = espnApiService.fetchLatestNews();
        if (response.isPresent()) {
            EspnNewsDto espnNewsDto = response.get();
            for (EspnNewsDto.Article article : espnNewsDto.getArticles()) {
                // skip if already exists
                if (articleRepository.existsByEspnId(article.getId())) {
                    continue;
                }

                String geminiResponse = geminiService.generateArticle(article.getHeadline(), article.getDescription());
                String thumbnailUrl = null;
                if (article.getImages() != null && !article.getImages().isEmpty()) {
                    thumbnailUrl = article.getImages().getFirst().getUrl();
                }

                articlesService.saveGeneratedArticle(article.getId(), article.getHeadline(), geminiResponse, thumbnailUrl);
            }
        }

    }
}
