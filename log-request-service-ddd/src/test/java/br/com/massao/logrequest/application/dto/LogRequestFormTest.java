package br.com.massao.logrequest.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LogRequestFormTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void givenValuesWhenBuildThenGetNewInstance() {
        LogRequestForm logRequest = LogRequestForm.builder().date(LocalDateTime.now()).ip("ip").request("request").status((short) 200).userAgent("userAgent").build();

        assertNotNull(logRequest);
    }
}