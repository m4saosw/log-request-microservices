package br.com.massao.logrequest.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatterUtil {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static LocalDateTime localDateTimeFrom(String str) {
        return LocalDateTime.parse(str, DateFormatterUtil.FORMATTER);
    }
}
