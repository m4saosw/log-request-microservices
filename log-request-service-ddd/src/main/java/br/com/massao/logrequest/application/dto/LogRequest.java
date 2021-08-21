package br.com.massao.logrequest.application.dto;

import br.com.massao.logrequest.application.util.CustomDateSerializer;
import br.com.massao.logrequest.application.util.DateFormatterUtil;
import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.infrastructure.model.LogRequestModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.data.domain.Page;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Log Request output data
 */
@NoArgsConstructor // Used by Jackson
@AllArgsConstructor
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class LogRequest {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateFormatterUtil.DATE_PATTERN)
    @JsonSerialize(using = CustomDateSerializer.class)
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


    // TODO - remover apos concluir a migracao
    public LogRequest(LogRequestModel model) {
        this.id = model.getId();
        this.date = model.getDate();
        this.ip = model.getIp();
        this.request = model.getRequest();
        this.status = model.getStatus();
        this.userAgent = model.getUserAgent();
    }

    public LogRequest(DomainLogRequest domain) {
        this.id = domain.getId();
        this.date = domain.getDate();
        this.ip = domain.getIp();
        this.request = domain.getRequest();
        this.status = domain.getStatus();
        this.userAgent = domain.getUserAgent();
    }

    /**
     * Convert from Model to Dto with pagination
     *
     * @param model
     * @return
     */
    public Page<LogRequest> listLogRequestFrom(Page<LogRequestModel> model) {
        return model.map(LogRequest::new);
    }
}
