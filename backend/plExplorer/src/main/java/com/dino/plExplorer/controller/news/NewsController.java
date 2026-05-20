package com.dino.plExplorer.controller.news;

import com.dino.plExplorer.dto.response.news.NewsResponse;
import com.dino.plExplorer.service.articles.ArticlesService;
import com.dino.plExplorer.service.gemini.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {
    private final GeminiService geminiService;
    private final ArticlesService articlesService;

     @GetMapping("/latest")
    public NewsResponse generateNewsArticle() {
         return articlesService.getLatestNews().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Latest news not found"));
     }
}
