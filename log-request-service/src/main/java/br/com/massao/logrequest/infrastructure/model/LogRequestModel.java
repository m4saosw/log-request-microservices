package br.com.massao.logrequest.infrastructure.model;

import br.com.massao.logrequest.application.util.CustomDateSerializer;
import br.com.massao.logrequest.application.util.DateFormatterUtil;
import br.com.massao.logrequest.domain.DomainLogRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Log Request persistence model
 */
@Entity
@Table(name = "logrequest")
@AllArgsConstructor
@NoArgsConstructor // used by JPA
@Data
@Builder
public class LogRequestModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // todo - customizar sequence?
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateFormatterUtil.DATE_PATTERN, timezone = "Brazil/East")
    @JsonSerialize(using = CustomDateSerializer.class)
    private LocalDateTime date;

    @Column(name = "ip")
    @NotEmpty
    private String ip;

    @Column(name = "request")
    @NotEmpty
    private String request;

    @Column(name = "status")
    @Min(0)
    private short status;

    @Column(name = "user_agent")
    @NotEmpty
    private String userAgent;


    public LogRequestModel(DomainLogRequest model) {
        this.id = model.getId();
        this.date = model.getDate();
        this.ip = model.getIp();
        this.request = model.getRequest();
        this.status = model.getStatus();
        this.userAgent = model.getUserAgent();
    }

    public DomainLogRequest toDomain() {
        if (Objects.isNull(this)) return null;

        return DomainLogRequest.builder()
                .id(this.getId())
                .date(this.getDate())
                .ip(this.getIp())
                .request(this.getRequest())
                .status(this.getStatus())
                .userAgent(this.getUserAgent())
                .build();
    }
}
