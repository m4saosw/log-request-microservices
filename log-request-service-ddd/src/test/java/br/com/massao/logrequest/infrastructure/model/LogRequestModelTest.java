package br.com.massao.logrequest.infrastructure.model;

import br.com.massao.logrequest.application.dto.LogRequestForm;
import br.com.massao.logrequest.application.util.DateFormatterUtil;
import br.com.massao.logrequest.domain.DomainLogRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LogRequestModelTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void givenValuesWhenBuildThenGetNewInstance() {
        LogRequestModel logRequest = LogRequestModel.builder().id(1L).date(LocalDateTime.now()).ip("ip").request("request").status((short) 200).userAgent("userAgent").build();

        assertNotNull(logRequest);
    }


    @Test
    void givenDomainWhenNewModelThenReturnModel() {
        // given
        LocalDateTime localDateTime = DateFormatterUtil.localDateTimeFrom("2021-07-17 01:01:01.001");
        LogRequestForm form = LogRequestForm.builder().date(localDateTime).ip("ip1").request("request").status((short) 200).userAgent("userAgent").build();
        DomainLogRequest domain = form.toDomain();

        // when
        LogRequestModel model = new LogRequestModel(domain);

        // then
        assertEquals(form.getDate(), model.getDate());
        assertEquals(form.getIp(), model.getIp());
        assertEquals(form.getRequest(), model.getRequest());
        assertEquals(form.getStatus(), model.getStatus());
        assertEquals(form.getUserAgent(), model.getUserAgent());
    }

    @Test
    void givenNullDomainWhenNewModelThenReturnNull() {
        // given
        DomainLogRequest domain = null;

        // when
        LogRequestModel model = new LogRequestModel(domain);

        // then
        assertNull(model.getId());

    }
}