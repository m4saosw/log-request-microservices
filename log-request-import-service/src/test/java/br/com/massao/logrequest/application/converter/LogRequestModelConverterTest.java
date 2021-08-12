package br.com.massao.logrequest.application.converter;

import br.com.massao.logrequest.application.dto.LogRequestForm;
import br.com.massao.logrequest.infrastructure.model.LogRequestModel;
import br.com.massao.logrequest.application.util.DateFormatterUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LogRequestModelConverterTest {

    @Test
    void givenFormWhenModelFromThenReturnModel() {
        // given
        LocalDateTime localDateTime = DateFormatterUtil.localDateTimeFrom("2021-07-17 01:01:01.001");
        LogRequestForm form = LogRequestForm.builder().date(localDateTime).ip("ip1").request("request").status((short) 200).userAgent("userAgent").build();

        // when
        LogRequestModel model = new LogRequestModelConverter().modelFrom(form);

        // then
        assertEquals(form.getDate(), model.getDate());
        assertEquals(form.getIp(), model.getIp());
        assertEquals(form.getRequest(), model.getRequest());
        assertEquals(form.getStatus(), model.getStatus());
        assertEquals(form.getUserAgent(), model.getUserAgent());
    }

    @Test
    void givenNullFormWhenModelFromThenReturnNull() {
        // given
        LogRequestForm form = null;

        // when
        LogRequestModel model = new LogRequestModelConverter().modelFrom(form);

        // then
        assertNull(model);

    }
}