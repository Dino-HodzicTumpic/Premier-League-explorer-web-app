package com.dino.plExplorer.mapper;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.*;

@Component
public class DateMapper {

    @Named("yearMonthToStartDate")
    public static LocalDate yearMonthToStartDate(YearMonth start) {
        return start != null ? start.atDay(1) : null;
    }

    @Named("yearMonthToUntilDate")
    public static LocalDate yearMonthToUntilDate(YearMonth until) {
        return until != null ? until.atEndOfMonth() : null;
    }

    @Named("toOffsetDateTime")
    public  OffsetDateTime toOffsetDateTime(LocalDateTime utcDate) {
        return utcDate.atOffset(ZoneOffset.UTC);
    }
}
