package br.com.massao.logrequest.domain;

import br.com.massao.logrequest.application.util.DateFormatterUtil;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@ToString
public class LogRequestParams {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String ip;
    private String request;
    private Short status;
    private String userAgent;

    public LogRequestParams(String startDate, String endDate, String ip, String request, String status, String userAgent) {
        if (Objects.nonNull(startDate))
            this.startDate = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern(DateFormatterUtil.DATE_PATTERN_QUERY));
        if (Objects.nonNull(endDate))
            this.endDate = LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern(DateFormatterUtil.DATE_PATTERN_QUERY));
        this.ip = ip;
        this.request = request;
        this.status = Objects.nonNull(status) ? Short.parseShort(status) : null;
        this.userAgent = userAgent;
    }

    public boolean haveStartDate() {
        return Objects.nonNull(this.startDate);
    }

    public boolean haveEndDate() {
        return Objects.nonNull(this.endDate);
    }

    public boolean haveIp() {
        return Objects.nonNull(this.ip);
    }

    public boolean haveRequests() {
        return Objects.nonNull(this.request);
    }

    public boolean haveStatus() {
        return Objects.nonNull(this.status);
    }

    public boolean haveUserAgent() {
        return Objects.nonNull(this.userAgent);
    }
}
