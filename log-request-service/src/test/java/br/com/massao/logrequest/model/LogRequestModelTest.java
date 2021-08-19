package br.com.massao.logrequest.model;

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


//    @Test
//    void givenFormWhenModelFromThenReturnModel() {
//        // given
//        LocalDateTime localDateTime = DateFormatterUtil.localDateTimeFrom("2021-07-17 01:01:01.001");
//        LogRequestForm form = LogRequestForm.builder().date(localDateTime).ip("ip1").request("request").status((short) 200).userAgent("userAgent").build();
//
//        // when
//        LogRequestModel model = new LogRequestModel(form);
//
//        // then
//        assertEquals(form.getDate(), model.getDate());
//        assertEquals(form.getIp(), model.getIp());
//        assertEquals(form.getRequest(), model.getRequest());
//        assertEquals(form.getStatus(), model.getStatus());
//        assertEquals(form.getUserAgent(), model.getUserAgent());
//    }
//
//    @Test
//    void givenNullFormWhenModelFromThenReturnNull() {
//        // given
//        LogRequestForm form = null;
//
//        // when
//        LogRequestModel model = new LogRequestModelConverter().modelFrom(form);
//
//        // then
//        assertNull(model);
//
//    }
}