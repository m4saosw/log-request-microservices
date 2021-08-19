package br.com.massao.logrequest.application.resource;

import br.com.massao.logrequest.application.dto.LogRequest;
import br.com.massao.logrequest.application.dto.LogRequestForm;
import br.com.massao.logrequest.application.util.DateFormatterUtil;
import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.domain.NotFoundException;
import br.com.massao.logrequest.domain.service.LogRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({LogRequestResource.class})  //  Auto-configure the Spring MVC infrastructure for unit tests
class LogRequestResourceTest {
    public static final String URL_LIST = "/v1/log-requests";
    public static final String URL_FIND_BY_ID = "/v1/log-requests/{id}";
    public static final String URL_ADD = "/v1/log-requests";
    public static final String URL_UPDATE = "/v1/log-requests/{id}";
    public static final String URL_SEARCH = "/v1/log-requests/search";

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
        DomainLogRequest log1 = new DomainLogRequest(1L, localDateTime, "ip1", "request", (short) 200, "userAgent");
        DomainLogRequest log2 = new DomainLogRequest(2L, localDateTime, "ip2", "request", (short) 200, "userAgent");

        List<DomainLogRequest> domains = Arrays.asList(log1, log2);
        //Page<DomainLogRequest> pageModel = new PageImpl<>(domains);

        // when
        //given(service.list(any(Pageable.class))).willReturn(pageModel);
        given(service.list()).willReturn(domains);

        // then
        mvc.perform(get(URL_LIST)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));
                //.andExpect(jsonPath("$['content']", hasSize(2)))
//                .andExpect(jsonPath("$.length()", is(2)))
//                .andExpect(jsonPath("$.last", is(true))) // com paginacao
//                .andExpect(jsonPath("$.totalPages", is(1))) // com paginacao
//                .andExpect(jsonPath("$.content[0].id", is(log1.getId().intValue())))
//                .andExpect(jsonPath("$.content[0].date").value(log1.getDate().format(DateFormatterUtil.FORMATTER)))
//                .andExpect(jsonPath("$.content[0].request").value(log1.getRequest()))
//                .andExpect(jsonPath("$.content[0].status").value(Integer.valueOf(log1.getStatus())))
//                .andExpect(jsonPath("$.content[0].userAgent").value(log1.getUserAgent()))

//                .andExpect(jsonPath("$.content[1].id", is(log2.getId().intValue())))
//                .andExpect(jsonPath("$.content[1].date").value(log2.getDate().format(DateFormatterUtil.FORMATTER)))
//                .andExpect(jsonPath("$.content[1].request").value(log2.getRequest()))
//                .andExpect(jsonPath("$.content[1].status").value(Integer.valueOf(log2.getStatus())))
//                .andExpect(jsonPath("$.content[1].userAgent").value(log2.getUserAgent()));

    }

    /**
     * FIND BY ID TEST CASES
     */

    @Test
    void givenLogsWhenFindByIdLogThenReturnJsonLogWithStatus200() throws Exception {
        // given
        DomainLogRequest log1 = new DomainLogRequest(1L, LocalDateTime.now(), "ip1", "request", (short) 200, "userAgent");

        // when
        given(service.findById(log1.getId())).willReturn(log1);


        // then
        mvc.perform(get(URL_FIND_BY_ID, log1.getId())
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
        DomainLogRequest model = new DomainLogRequest(1L, LocalDateTime.now(), "ip1", "request", (short) 200, "userAgent");

        // when
        given(service.save(any(DomainLogRequest.class))).willReturn(model);

        // then
        String jsonObject = asJsonString(form);

        mvc.perform(post(URL_ADD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject))
                .andExpect(status().isCreated())
                .andExpect(content().string(""))
                .andExpect(header().exists("location"));
    }


    @Test
    void givenInvalidBodyWhenCreateLogThenReturnStatus400() throws Exception {
        // given
        DomainLogRequest model = null;

        // when
        given(service.save(any(DomainLogRequest.class))).willReturn(model);

        // then
        String jsonObject = asJsonString(model);

        mvc.perform(post(URL_ADD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject))
                .andExpect(status().isBadRequest());
    }


    @Test
    void givenInvalidLogWhenCreateLogThenReturnStatus400() throws Exception {
        // given
        DomainLogRequest model = DomainLogRequest.builder().build();

        // when
        given(service.save(any(DomainLogRequest.class))).willReturn(model);

        // then
        String jsonObject = asJsonString(model);

        mvc.perform(post(URL_ADD)
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
        DomainLogRequest model = new DomainLogRequest(1L, localDateTime, "ip1", "request", (short) 200, "userAgent");
        LogRequest log = new LogRequest(1L, localDateTime, "ip1", "request", (short) 200, "userAgent");

        // when
        given(service.update(anyLong(), any())).willReturn(model);

        // then
        String jsonObject = asJsonString(new LogRequest(model));
        mvc.perform(put(URL_UPDATE, log.getId())
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

    @Test
    void givenNotFoundWhenUpdateLogThenReturnStatus404() throws Exception {
        // given
        LocalDateTime localDateTime = DateFormatterUtil.localDateTimeFrom("2021-07-17 01:01:01.001");
        LogRequestForm person1 = new LogRequestForm(localDateTime, "ip1", "request", (short) 200, "userAgent");

        // when
        given(service.update(anyLong(), any())).willThrow(NotFoundException.class);

        // then
        String jsonObject = asJsonString(person1);
        mvc.perform(put(URL_UPDATE, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }


    /**
     * SEARCH TEST CASES
     */

//    @Test
//    void givenQueriesStartDateWhenGetSearchLogsThenReturnJsonLogsWithStatus200() throws Exception {
//        // given
//        String dateStr = "2021-07-17 01:01:01.001";
//        String dateStr2 = "2021-07-17 01:01:01";
//        LocalDateTime localDateTime = DateFormatterUtil.localDateTimeFrom(dateStr);
//        DomainLogRequest log1 = new DomainLogRequest(1L, localDateTime, "ip1", "request", (short) 200, "userAgent");
//
//        List<DomainLogRequest> model = Arrays.asList(log1);
//        Page<DomainLogRequest> pageModel = new PageImpl<>(model);
//
//        final LogRequestParams params = new LogRequestParams(dateStr2, null, null, null, null, null);
//
//        // when
//        given(service.searchByFilters(any(Specification.class), any(Pageable.class))).willReturn(pageModel);
//
//        // then
//        mvc.perform(get(URL_SEARCH)
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("startDate", params.getStartDate().format(DateFormatterUtil.FORMATTER_QUERY))
//        )
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$['content']", hasSize(1)))
//                .andExpect(jsonPath("$.last", is(true))) // com paginacao
//                .andExpect(jsonPath("$.totalPages", is(1))) // com paginacao
//                .andExpect(jsonPath("$.content[0].id", is(log1.getId().intValue())))
//                .andExpect(jsonPath("$.content[0].date").value(log1.getDate().format(DateFormatterUtil.FORMATTER)))
//                .andExpect(jsonPath("$.content[0].request").value(log1.getRequest()))
//                .andExpect(jsonPath("$.content[0].status").value(Integer.valueOf(log1.getStatus())))
//                .andExpect(jsonPath("$.content[0].userAgent").value(log1.getUserAgent()));
//    }


//    @Test
//    void givenQueriesAllWhenGetSearchLogsThenReturnJsonLogsWithStatus200() throws Exception {
//        // given
//        String dateStr = "2021-07-17 01:01:01.001";
//        String dateStr2 = "2021-07-17 01:01:01";
//        LocalDateTime localDateTime = DateFormatterUtil.localDateTimeFrom(dateStr);
//        DomainLogRequest log1 = new DomainLogRequest(1L, localDateTime, "ip1", "request", (short) 200, "userAgent");
//
//        List<DomainLogRequest> model = Arrays.asList(log1);
//        Page<DomainLogRequest> pageModel = new PageImpl<>(model);
//
//        final LogRequestParams params = new LogRequestParams(dateStr2, dateStr2, log1.getIp(), log1.getRequest(), Short.toString(log1.getStatus()), log1.getUserAgent());
//
//        // when
//        given(service.searchByFilters(any(Specification.class), any(Pageable.class))).willReturn(pageModel);
//
//        // then
//        mvc.perform(get(URL_SEARCH)
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("startDate", params.getStartDate().format(DateFormatterUtil.FORMATTER_QUERY))
//                .param("endDate", params.getStartDate().format(DateFormatterUtil.FORMATTER_QUERY))
//                .param("ip", params.getIp())
//                .param("request", params.getRequest())
//                .param("status", Short.toString(params.getStatus()))
//                .param("userAgent", params.getUserAgent()))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$['content']", hasSize(1)))
//                .andExpect(jsonPath("$.last", is(true))) // com paginacao
//                .andExpect(jsonPath("$.totalPages", is(1))) // com paginacao
//                .andExpect(jsonPath("$.content[0].id", is(log1.getId().intValue())))
//                .andExpect(jsonPath("$.content[0].date").value(log1.getDate().format(DateFormatterUtil.FORMATTER)))
//                .andExpect(jsonPath("$.content[0].request").value(log1.getRequest()))
//                .andExpect(jsonPath("$.content[0].status").value(Integer.valueOf(log1.getStatus())))
//                .andExpect(jsonPath("$.content[0].userAgent").value(log1.getUserAgent()));
//    }


//    @Test
//    void givenNoQueriesWhenGetSearchLogsThenReturnJsonLogsWithStatus400() throws Exception {
//        // given
//        Page<DomainLogRequest> pageModel = new PageImpl<>(Arrays.asList());
//
//        final LogRequestParams params = new LogRequestParams(null, null, null, null, null, null);
//        final Specification<DomainLogRequest> specification = new LogRequestSpecifications().fromParams(params);
//
//        // when
//        given(service.searchByFilters(eq(specification), any(Pageable.class))).willReturn(pageModel);
//
//        // then
//        mvc.perform(get(URL_SEARCH)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
}