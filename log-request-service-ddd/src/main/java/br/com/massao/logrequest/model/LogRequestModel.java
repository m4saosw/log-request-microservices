package br.com.massao.logrequest.model;

import br.com.massao.logrequest.application.util.CustomDateSerializer;
import br.com.massao.logrequest.application.util.DateFormatterUtil;
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
}
