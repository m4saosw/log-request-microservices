package br.com.massao.logrequest.infrastructure.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LogRequestModelTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void givenValuesWhenBuildThenGetNewInstance() {
        LogRequestModel logRequest = LogRequestModel.builder().id(1L).date(LocalDateTime.now()).ip("ip").request("request").status((short) 200).userAgent("userAgent").build();

        assertNotNull(logRequest);
    }
}