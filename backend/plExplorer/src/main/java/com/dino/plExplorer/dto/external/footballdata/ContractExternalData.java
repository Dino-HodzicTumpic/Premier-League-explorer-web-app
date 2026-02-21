package com.dino.plExplorer.dto.external.footballdata;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractExternalData {

    // Football-data.org vraca "2021-08" format (YYYY-MM)
    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth start;

    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth until;


}
