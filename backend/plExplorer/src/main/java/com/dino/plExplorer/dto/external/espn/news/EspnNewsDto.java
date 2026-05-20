package com.dino.plExplorer.dto.external.espn.news;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EspnNewsDto {
    private List<Article> articles;

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Article {
        private String headline;
        private String description;
        private Long id;
        private List<Image> images;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        private String url;
    }
}
