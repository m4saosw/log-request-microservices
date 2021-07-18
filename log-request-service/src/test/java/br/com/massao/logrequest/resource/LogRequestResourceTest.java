package br.com.massao.logrequest.resource;

import br.com.massao.logrequest.dto.LogRequest;
import br.com.massao.logrequest.model.LogRequestModel;
import br.com.massao.logrequest.util.DateFormatterUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LogRequestResource.class)  //  Auto-configure the Spring MVC infrastructure for unit tests
class LogRequestResourceTest {
    @Autowired
    private MockMvc mvc;

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
        LogRequest log1 = new LogRequest(1L, localDateTime, "ip1", "request", (short) 200, "userAgent");
        LogRequest log2 = new LogRequest(2L, localDateTime, "ip2", "request", (short) 200, "userAgent");

        // when


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
        LogRequest log1 = new LogRequest(1L, LocalDateTime.now(), "ip1", "request", (short) 200, "userAgent");

        // when


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
        LogRequest log = new LogRequest(1L, LocalDateTime.now(), "ip1", "request", (short) 200, "userAgent");

        // when

        // then
        String jsonObject = asJsonString(log);

        mvc.perform(post("/v1/log-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject))
                .andExpect(status().isCreated())
                .andExpect(content().string(""))
                .andExpect(header().exists("location"));
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