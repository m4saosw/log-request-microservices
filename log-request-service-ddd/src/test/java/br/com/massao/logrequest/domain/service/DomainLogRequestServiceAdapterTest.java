package br.com.massao.logrequest.domain.service;

import br.com.massao.logrequest.application.util.DateFormatterUtil;
import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.domain.DomainLogRequestParams;
import br.com.massao.logrequest.domain.NotFoundException;
import br.com.massao.logrequest.domain.repository.DomainLogRequestRepositoryPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class DomainLogRequestServiceAdapterTest {

    //@MockBean
    @Mock
    DomainLogRequestRepositoryPort repositoryPort;

    @InjectMocks
    private DomainLogRequestServiceAdapter service;

    private LocalDateTime localDateTime = DateFormatterUtil.localDateTimeFrom("2021-07-17 01:01:01.001");
    private DomainLogRequest log1 = new DomainLogRequest(1L, localDateTime, "ip1", "request", (short) 200, "userAgent");
    private DomainLogRequest log2 = new DomainLogRequest(2L, localDateTime, "ip2", "request", (short) 200, "userAgent");


    @BeforeEach
    void setUp() {
    }

    /**
     * LIST TEST CASES
     */

    @Test
    void givenLogsWhenListThenReturnLogs() {
        // given
        Page<DomainLogRequest> pageDomain = new PageImpl<>(Arrays.asList(log1, log2));
        given(repositoryPort.listAll(any(Pageable.class))).willReturn(pageDomain);

        // when
        Page<DomainLogRequest> result = service.list(pageDomain.getPageable());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().contains(log1)).isTrue();
        assertThat(result.getContent().contains(log2)).isTrue();
    }


    /**
     * FIND BY ID TEST CASES
     */

    @Test
    void givenLogsWhenFindByIdLogThenReturnLog() throws NotFoundException {
        // given
        DomainLogRequest log1 = new DomainLogRequest(1L, localDateTime, "ip1", "request", (short) 200, "userAgent");

        given(repositoryPort.findById(log1.getId())).willReturn(Optional.of(log1));

        // when
        DomainLogRequest result = service.findById(log1.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(log1.getId());
    }

    @Test
    void givenLogNotFoundWhenFindByIdLogThenThrowsNotFoundException() {
        // given
        Long id = 9999999999999L;
        when(repositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // when/then
        Assertions.assertThatExceptionOfType(NotFoundException.class).isThrownBy(
                () -> service.findById(id));
    }


    /**
     * CREATE TEST CASES
     */

    @Test
    void givenLogWhenCreateLogThenReturnLog() {
        // given
        given(repositoryPort.create(log1)).willReturn(log1);

        // when
        DomainLogRequest result = service.save(log1);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(log1.getId());
    }


    /**
     * EDIT TEST CASES
     */

    @Test
    void givenLogWhenEditLogThenReturnLog() throws NotFoundException {
        // given
        given(repositoryPort.update(anyLong(), any(DomainLogRequest.class))).willReturn(log1);

        // when
        DomainLogRequest result = service.update(log1.getId(), log1);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(log1.getId());
        //verify(repositoryPort).findById(any());
        //verify(repositoryPort).update(anyLong(), any(DomainLogRequest.class));
    }

    @Test
    void givenLogNotFoundWhenEditLogThenThrowsNotFoundException() throws NotFoundException {
        // given
        // given(repositoryPort.findById(log1.getId())).willReturn(Optional.empty());
        when(repositoryPort.update(anyLong(), any())).thenThrow(NotFoundException.class);

        // when/then
        Assertions.assertThatExceptionOfType(NotFoundException.class).isThrownBy(
                () -> service.update(log1.getId(), null));
    }


    /**
     * SEARCH TEST CASES
     */

    @Test
    void givenLogsWhenSearchThenReturnLogs() {
        // given
        String dateStr = "2021-07-17 01:01:01.001";
        String dateStr2 = "2021-07-17 01:01:01";
        LocalDateTime localDateTime = DateFormatterUtil.localDateTimeFrom(dateStr);
        DomainLogRequest log1 = new DomainLogRequest(1L, localDateTime, "ip1", "request1", (short) 200, "userAgent1");
        DomainLogRequest log2 = new DomainLogRequest(2L, localDateTime, "ip2", "request2", (short) 200, "userAgent2");

        List<DomainLogRequest> domains = Arrays.asList(log1, log2);
        Page<DomainLogRequest> pageDomain = new PageImpl<>(domains);

        final DomainLogRequestParams params = new DomainLogRequestParams(dateStr2, dateStr2, log1.getIp(), log1.getRequest(), Short.toString(log1.getStatus()), log1.getUserAgent());

        given(repositoryPort.listAll(any(DomainLogRequestParams.class), any(Pageable.class))).willReturn(pageDomain);

        // when
        Page<DomainLogRequest> result = service.searchByFilters(params, pageDomain.getPageable());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().contains(log1)).isTrue();
        assertThat(result.getContent().contains(log2)).isTrue();
    }


    /**
     * TestConfiguration guarantee this bean is only for test scope
     */
//    @TestConfiguration
//    static class LogRequestServiceTestContextConfiguration {
////        @Bean
////        LogRequestService logRequestService() {
////            return new LogRequestServiceImpl();
////
////        }
//
//
////        @Bean
////        Validator validator() {
////            return new LocalValidatorFactoryBean();
////        }
//    }
}