package br.com.massao.logrequest.resource;

import br.com.massao.logrequest.dto.LogRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        LogRequest log1 = new LogRequest(1L, LocalDateTime.now(), "ip1", "request", (short) 200, "userAgent");
        LogRequest log2 = new LogRequest(2L, LocalDateTime.now(), "ip2", "request", (short) 200, "userAgent");

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
                .andExpect(jsonPath("$.content[1].id", is(log2.getId().intValue())));
    }


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
}