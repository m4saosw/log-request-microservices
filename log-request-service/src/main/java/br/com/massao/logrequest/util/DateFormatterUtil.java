package br.com.massao.logrequest.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatterUtil {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final String DATE_PATTERN_QUERY = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER_QUERY = DateTimeFormatter.ofPattern(DATE_PATTERN_QUERY);

    public static LocalDateTime localDateTimeFrom(String str) {
        return LocalDateTime.parse(str, DateFormatterUtil.FORMATTER);
    }
}
