package br.com.massao.logrequest.resource;

import br.com.massao.logrequest.LogRequestServiceApplicationDDD;
import br.com.massao.logrequest.dto.LogRequest;
import br.com.massao.logrequest.dto.LogRequestForm;
import br.com.massao.logrequest.model.LogRequestModel;
import br.com.massao.logrequest.util.DateFormatterUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = LogRequestServiceApplicationDDD.class)
@AutoConfigureMockMvc
class LogRequestResourceIntegrationTest {
    @Autowired
    private MockMvc mvc;

    LocalDateTime localDateTime = DateFormatterUtil.localDateTimeFrom("2021-07-17 01:01:01.001");
    LogRequestModel model1 = new LogRequestModel(1L, localDateTime, "ip1", "request1", (short) 200, "userAgent1");
    LogRequestModel model2 = new LogRequestModel(2L, localDateTime, "ip2", "request2", (short) 201, "userAgent2");
    LogRequestForm form1 = new LogRequestForm(localDateTime, "ip1", "request1", (short) 200, "userAgent1");
    LogRequestForm form2 = new LogRequestForm(localDateTime, "ip2", "request2", (short) 201, "userAgent2");

    @Autowired
    LogRequestResource controller;


    @BeforeEach
    void setUp() {
    }

    @Test
    void createFindEditList() throws Exception {
        this.create(form1);
        this.findById(model1.getId(), model1);

        this.edit(model1.getId(), form2);
        this.findById(model1.getId(), model2);

        this.list();
    }


    ResponseEntity<Page<LogRequest>> list() {
        Pageable pageable = PageRequest.of(0, 1, Sort.unsorted());
        ResponseEntity<Page<LogRequest>> result = controller.list(pageable);

        assertThat(result.getBody().getSize()).isEqualTo(1);

        return result;

    }


    void findById(Long id, LogRequestModel model) throws Exception {
        ResponseEntity<LogRequest> result1 = controller.findById(id);
        LogRequest result = result1.getBody();

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getDate()).isEqualTo(model.getDate());
        assertThat(result.getIp()).isEqualTo(model.getIp());
        assertThat(result.getRequest()).isEqualTo(model.getRequest());
        assertThat(result.getStatus()).isEqualTo(model.getStatus());
        assertThat(result.getUserAgent()).isEqualTo(model.getUserAgent());
    }

    void create(LogRequestForm form) {
        controller.create(form);
    }


    void edit(Long id, LogRequestForm form) throws Exception {
        controller.update(id, form);
    }
}