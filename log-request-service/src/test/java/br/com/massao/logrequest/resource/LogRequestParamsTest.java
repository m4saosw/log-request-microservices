package br.com.massao.logrequest.resource;

import br.com.massao.logrequest.domain.LogRequestParams;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class LogRequestParamsTest {

    @Test
    void givenNullValuesWhenCreateThenReturnNull() {
        LogRequestParams params = new LogRequestParams(null, null, null, null, null, null);

        assertNull(params.getStartDate());
        assertNull(params.getEndDate());
        assertNull(params.getIp());
        assertNull(params.getRequest());
        assertNull(params.getStatus());
        assertNull(params.getUserAgent());
    }
}