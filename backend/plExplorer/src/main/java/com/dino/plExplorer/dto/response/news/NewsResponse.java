package com.dino.plExplorer.dto.response.news;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {
    private List<Article> news;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Article{
        private Long id;
        private String headline;
        private String body;
        private String thumbnailUrl;
        private String dateCreated;
    }
}
