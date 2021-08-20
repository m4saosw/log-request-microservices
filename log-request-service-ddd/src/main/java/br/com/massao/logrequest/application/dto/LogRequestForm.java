package br.com.massao.logrequest.application.dto;

import br.com.massao.logrequest.util.CustomDateSerializer;
import br.com.massao.logrequest.util.DateFormatterUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Log Request input data
 */
@NoArgsConstructor // Used by Jackson
@AllArgsConstructor
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class LogRequestForm {

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateFormatterUtil.DATE_PATTERN)
    @JsonSerialize(using = CustomDateSerializer.class)
    @NotNull
    private LocalDateTime date;

    @JsonProperty("ip")
    @NotEmpty
    @NotNull
    private String ip;

    @JsonProperty("request")
    @NotEmpty
    @NotNull
    private String request;

    @JsonProperty("status")
    @Min(0)
    @NotNull
    private short status;

    @JsonProperty("userAgent")
    @NotEmpty
    @NotNull
    private String userAgent;
}
