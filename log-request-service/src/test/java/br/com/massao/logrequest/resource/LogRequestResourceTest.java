package br.com.massao.logrequest.resource;

import br.com.massao.logrequest.converter.LogRequestModelConverter;
import br.com.massao.logrequest.dto.LogRequest;
import br.com.massao.logrequest.dto.LogRequestForm;
import br.com.massao.logrequest.model.LogRequestModel;
import br.com.massao.logrequest.service.LogRequestService;
import br.com.massao.logrequest.util.DateFormatterUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({LogRequestResource.class, LogRequestModelConverter.class})  //  Auto-configure the Spring MVC infrastructure for unit tests
class LogRequestResourceTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private LogRequestService service;

    @BeforeEach
    void setUp() {
    }


    /**
     * LIST TEST CASES
     */

    @Test
    void givenLogsWhenGetLogsThenReturnJsonLogsWithStatus200() throws Exception {
        // given
        LocalDateTime localDateTime = DateFormatterUtil.localDateTimeFrom("2021-07-17 01:01:01.001");
        LogRequestModel log1 = new LogRequestModel(1L, localDateTime, "ip1", "request", (short) 200, "userAgent");
        LogRequestModel log2 = new LogRequestModel(2L, localDateTime, "ip2", "request", (short) 200, "userAgent");

        List<LogRequestModel> model = Arrays.asList(log1, log2);
        Page<LogRequestModel> pageModel = new PageImpl<>(model);

        // when
        given(service.list(any(Pageable.class))).willReturn(pageModel);

        // then
        mvc.perform(get("/v1/log-requests")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$['content']", hasSize(2)))
                .andExpect(jsonPath("$.last", is(true))) // com paginacao
                .andExpect(jsonPath("$.totalPages", is(1))) // com paginacao
                .andExpect(jsonPath("$.content[0].id", is(log1.getId().intValue())))
                .andExpect(jsonPath("$.content[0].date").value(log1.getDate().format(DateFormatterUtil.FORMATTER)))
                .andExpect(jsonPath("$.content[0].request").value(log1.getRequest()))
                .andExpect(jsonPath("$.content[0].status").value(Integer.valueOf(log1.getStatus())))
                .andExpect(jsonPath("$.content[0].userAgent").value(log1.getUserAgent()))

                .andExpect(jsonPath("$.content[1].id", is(log2.getId().intValue())))
                .andExpect(jsonPath("$.content[1].date").value(log2.getDate().format(DateFormatterUtil.FORMATTER)))
                .andExpect(jsonPath("$.content[1].request").value(log2.getRequest()))
                .andExpect(jsonPath("$.content[1].status").value(Integer.valueOf(log2.getStatus())))
                .andExpect(jsonPath("$.content[1].userAgent").value(log2.getUserAgent()));

    }

    /**
     * FIND BY ID TEST CASES
     */

    @Test
    void givenLogsWhenFindByIdLogThenReturnJsonLogWithStatus200() throws Exception {
        // given
        LogRequestModel log1 = new LogRequestModel(1L, LocalDateTime.now(), "ip1", "request", (short) 200, "userAgent");

        // when
        given(service.findById(log1.getId())).willReturn(log1);


        // then
        mvc.perform(get("/v1/log-requests/{id}", log1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(log1.getId()));
    }

    /**
     * CREATE TEST CASES
     */

    @Test
    void givenLogWhenCreateLogThenReturnLocationWithStatus201() throws Exception {
        // given
        LogRequestForm form = new LogRequestForm(LocalDateTime.now(), "ip1", "request", (short) 200, "userAgent");
        LogRequestModel model = new LogRequestModel(1L, LocalDateTime.now(), "ip1", "request", (short) 200, "userAgent");

        // when
        given(service.save(any(LogRequestModel.class))).willReturn(model);

        // then
        String jsonObject = asJsonString(form);

        mvc.perform(post("/v1/log-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject))
                .andExpect(status().isCreated())
                .andExpect(content().string(""))
                .andExpect(header().exists("location"));
    }


    @Test
    void givenInvalidBodyWhenCreateLogThenReturnStatus400() throws Exception {
        // given
        LogRequestModel model = null;

        // when
        given(service.save(any(LogRequestModel.class))).willReturn(model);

        // then
        String jsonObject = asJsonString(model);

        mvc.perform(post("/v1/log-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject))
                .andExpect(status().isBadRequest());
    }


    @Test
    void givenInvalidLogWhenCreateLogThenReturnStatus400() throws Exception {
        // given
        LogRequestModel model = LogRequestModel.builder().build();

        // when
        given(service.save(any(LogRequestModel.class))).willReturn(model);

        // then
        String jsonObject = asJsonString(model);

        mvc.perform(post("/v1/log-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject))
                .andExpect(status().isBadRequest());
    }


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * EDIT TEST CASES
     */

    @Test
    void givenLogWhenUpdateThenReturnLogWithStatus200() throws Exception {
        // given
        LocalDateTime localDateTime = DateFormatterUtil.localDateTimeFrom("2021-07-17 01:01:01.001");
        LogRequestModel model = new LogRequestModel(1L, localDateTime, "ip1", "request", (short) 200, "userAgent");
        LogRequest log = new LogRequest(1L, localDateTime, "ip1", "request", (short) 200, "userAgent");

        // when

        // then
        String jsonObject = asJsonString(new LogRequest(model));
        mvc.perform(put("/v1/log-requests/{id}", log.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(log.getId()))
                .andExpect(jsonPath("$.date").value(log.getDate().format(DateFormatterUtil.FORMATTER)))
                .andExpect(jsonPath("$.request").value(log.getRequest()))
                .andExpect(jsonPath("$.status").value(Integer.valueOf(log.getStatus())))
                .andExpect(jsonPath("$.userAgent").value(log.getUserAgent()));
    }
}