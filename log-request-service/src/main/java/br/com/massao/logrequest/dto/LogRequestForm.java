package br.com.massao.logrequest.dto;

import br.com.massao.logrequest.util.DateFormatterUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor // Used by Jackson
@AllArgsConstructor
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class LogRequestForm {

    @JsonProperty("date")
    @JsonFormat(pattern = DateFormatterUtil.DATE_PATTERN)
    @NotNull
    private LocalDateTime date;

    @JsonProperty("ip")
    @NotEmpty
    private String ip;

    @JsonProperty("request")
    @NotEmpty
    private String request;

    @JsonProperty("status")
    @Min(0)
    private short status;

    @JsonProperty("userAgent")
    @NotEmpty
    private String userAgent;
}
