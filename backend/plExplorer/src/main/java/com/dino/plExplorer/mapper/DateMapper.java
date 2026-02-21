package com.dino.plExplorer.mapper;

import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.YearMonth;

public class DateMapper {

    @Named("yearMonthToStartDate")
    public static LocalDate yearMonthToStartDate(YearMonth start) {
        return start != null ? start.atDay(1) : null;
    }

    @Named("yearMonthToUntilDate")
    public static LocalDate yearMonthToUntilDate(YearMonth until) {
        return until != null ? until.atEndOfMonth() : null;
    }
}
