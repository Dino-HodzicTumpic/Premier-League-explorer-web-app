package com.dino.plExplorer.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Slf4j
public class TheSportsDbConfig {

    @Value("${theSportsDb.apiKey:123}")
    private String apiKey;

    @Value("${thesportsdb.api.base-url:https://www.thesportsdb.com/api/v1/json}")
    private String baseUrl;

    @Bean(name = "theSportsDbWebClient")
    public WebClient theSportsDbWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl + "/" + apiKey)
                .build();
    }

}
