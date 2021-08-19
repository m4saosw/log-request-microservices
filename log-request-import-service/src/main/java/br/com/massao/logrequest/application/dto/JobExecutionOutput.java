package br.com.massao.logrequest.application.dto;

import br.com.massao.logrequest.application.util.DateFormatterUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;

import java.time.LocalDateTime;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

@Builder
@Getter
public class JobExecutionOutput {
    @JsonProperty("jobId")
    private long jobId;

    @JsonProperty("startTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateFormatterUtil.DATE_PATTERN)
    private LocalDateTime startTime;

    @JsonProperty("endTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateFormatterUtil.DATE_PATTERN)
    private LocalDateTime endTime;

    @JsonProperty("exitCode")
    private String exitCode;

    @JsonProperty("exitDescription")
    private String exitDescription;

    @JsonProperty("status")
    private BatchStatus status;

    @JsonProperty("exceptions")
    private Collection<String> exceptions;


    public static JobExecutionOutput fromJobExecution(JobExecution je) {
        return JobExecutionOutput.builder()
                .jobId(je.getJobId())
                .startTime(DateFormatterUtil.localDateTime(je.getStartTime()))
                .endTime(DateFormatterUtil.localDateTime(je.getEndTime()))
                .exitCode(je.getExitStatus() == null ? null : je.getExitStatus().getExitCode())
                .exitDescription(je.getExitStatus() == null ? null : je.getExitStatus().getExitDescription())
                .status(je.getStatus())
                .exceptions(je.getFailureExceptions().stream().map(e -> e.getMessage() + ": " + e.getStackTrace().toString()).collect(toList()))
                .build();
    }
}
