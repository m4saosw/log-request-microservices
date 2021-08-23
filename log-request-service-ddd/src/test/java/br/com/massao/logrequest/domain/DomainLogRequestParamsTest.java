package br.com.massao.logrequest.domain;

import br.com.massao.logrequest.domain.DomainLogRequestParams;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class DomainLogRequestParamsTest {

    @Test
    void givenNullValuesWhenCreateThenReturnNull() {
        DomainLogRequestParams params = new DomainLogRequestParams(null, null, null, null, null, null);

        assertNull(params.getStartDate());
        assertNull(params.getEndDate());
        assertNull(params.getIp());
        assertNull(params.getRequest());
        assertNull(params.getStatus());
        assertNull(params.getUserAgent());
    }
}