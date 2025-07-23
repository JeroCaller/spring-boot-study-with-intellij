package com.jerocaller.QuartzStudy.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateMapper {

    public static LocalDate toLocalDate(Date oldDate) {
        return oldDate.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date oldDate) {
        return oldDate.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }

    public static String toDateTimeFormatString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss.AAA")
        );
    }
}
