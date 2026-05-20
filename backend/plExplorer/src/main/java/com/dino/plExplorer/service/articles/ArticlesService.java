package com.dino.plExplorer.service.articles;

import com.dino.plExplorer.dto.ImageUploadResult;
import com.dino.plExplorer.dto.response.news.NewsResponse;
import com.dino.plExplorer.entity.Article;
import com.dino.plExplorer.repository.ArticleRepository;
import com.dino.plExplorer.service.ImageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticlesService {
    private final ArticleRepository articleRepository;
    private final ObjectMapper objectMapper;
    private final ImageService imageService;

    @Transactional
    public Article saveGeneratedArticle(Long espnId, String fallbackHeadline, String geminiJson, String thumbnailUrl) {
        String headline = fallbackHeadline;
        String body = geminiJson;
        String resolvedThumbnailUrl = thumbnailUrl;

        if (thumbnailUrl != null && !thumbnailUrl.isBlank()) {
            resolvedThumbnailUrl = imageService.uploadImage(thumbnailUrl, "articles")
                    .map(ImageUploadResult::secureUrl)
                    .orElse(thumbnailUrl);
        }

        if (geminiJson != null && !geminiJson.isBlank()) {
            try {
                String jsonPayload = extractJsonPayload(geminiJson);
                JsonNode root = objectMapper.readTree(jsonPayload);
                String titleValue = root.path("title").asText(null);
                String bodyValue = root.path("body").asText(null);

                if (titleValue != null && !titleValue.isBlank()) {
                    headline = titleValue;
                }
                if (bodyValue != null && !bodyValue.isBlank()) {
                    body = bodyValue;
                }
            } catch (Exception e) {
                body = extractJsonPayload(geminiJson);
                log.warn("Failed to parse Gemini JSON for ESPN article {}", espnId, e);
            }
        }

        Article article = Article.builder()
                .espnId(espnId)
                .headline(headline)
                .body(body)
                .thumbnailUrl(resolvedThumbnailUrl)
                .build();

        return articleRepository.save(article);
    }

    public Optional<NewsResponse> getLatestNews() {
        List<Article> articles = articleRepository.findTop5ByOrderByDateCreatedDesc();
        if (articles.isEmpty()) {
            return Optional.empty();
        }

        List<NewsResponse.Article> newsArticles = articles.stream()
                .map(article ->NewsResponse.Article
                .builder()
                        .id(article.getId())
                        .headline(article.getHeadline())
                        .body(article.getBody())
                        .thumbnailUrl(article.getThumbnailUrl())
                        .dateCreated(article.getDateCreated().toString())
                        .build())
                .collect(Collectors.toList());

        return Optional.of(new NewsResponse(newsArticles));
    }

    private String extractJsonPayload(String raw) {
        String trimmed = raw.trim();
        trimmed = trimmed.replaceAll("(?s)^```[a-zA-Z]*\\s*", "");
        trimmed = trimmed.replaceAll("(?s)\\s*```$", "");
        trimmed = trimmed.trim();

        if (!trimmed.startsWith("{") || !trimmed.endsWith("}")) {
            int start = trimmed.indexOf('{');
            int end = trimmed.lastIndexOf('}');
            if (start >= 0 && end > start) {
                trimmed = trimmed.substring(start, end + 1);
            }
        }

        return trimmed.trim();
    }


}
