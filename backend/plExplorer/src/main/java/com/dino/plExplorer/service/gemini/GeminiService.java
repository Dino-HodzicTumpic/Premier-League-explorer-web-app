package com.dino.plExplorer.service.gemini;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class GeminiService {
    Client client;


    public String generateArticle(String headline, String description) {

        String prompt = """
                You are a professional football journalist.

                Write a detailed football news article based on:

                Headline:
                %s

                Description:
                %s

                Main requirement:
                - try to search the web and find more info about this headline and description
                  and write an article based on the info that you find

                Other Requirements:
                - Write naturally like a sports news website
                - Use engaging journalistic style
                - Do not invent fake quotes
                - Return ONLY valid JSON, no markdown, no extra text
                - JSON schema: {"title": "...", "body": "..."}
                - "title" should be a polished headline based on the input
                - "body" should be the full article text
                """.formatted(headline, description);

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        prompt,
                        null);

        System.out.println("Gemini response: " + response.text());
        return response.text();
    }

}
