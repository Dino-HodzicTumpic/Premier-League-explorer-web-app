package com.dino.plExplorer.dto.external.espn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class EspnPlayersResponseDto {
    @JsonProperty("athletes")
    private List<EspnPlayerDto> players;
}
