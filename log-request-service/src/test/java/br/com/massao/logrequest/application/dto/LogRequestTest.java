package br.com.massao.logrequest.application.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;


class LogRequestTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void givenValuesWhenBuildThenGetNewInstance() {
        LogRequest logRequest = LogRequest.builder().id(1L).date(LocalDateTime.now()).ip("ip").request("request").status((short) 200).userAgent("userAgent").build();

        assertNotNull(logRequest);
    }
}