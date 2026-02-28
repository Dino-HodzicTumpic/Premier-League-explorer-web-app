package com.dino.plExplorer.dto.external.sportsDb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SportsDbPlayerResponse {

    @JsonProperty("player")
    private List<PlayerData> player;//player jer je tako external api vraca listu player:

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class PlayerData{
        @JsonProperty("strCutout")
        private String strCutout;
    }
}
