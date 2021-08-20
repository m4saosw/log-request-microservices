package br.com.massao.logrequest.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Log Request domain
 */
@AllArgsConstructor
@Data
@Builder
public class DomainLogRequest {
    private Long id;
    private LocalDateTime date;
    private String ip;
    private String request;
    private short status;
    private String userAgent;
}
