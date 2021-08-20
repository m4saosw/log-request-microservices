package br.com.massao.logrequest.service;

import br.com.massao.logrequest.application.exception.NotFoundException;
import br.com.massao.logrequest.model.LogRequestModel;
import br.com.massao.logrequest.infrastructure.repository.LogRequestRepository;
import br.com.massao.logrequest.application.util.DateFormatterUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class LogRequestServiceTest {

    @Autowired
    private LogRequestService service;

    @MockBean
    private LogRequestRepository repository;

    private LocalDateTime localDateTime = DateFormatterUtil.localDateTimeFrom("2021-07-17 01:01:01.001");
    private LogRequestModel log1 = new LogRequestModel(1L, localDateTime, "ip1", "request", (short) 200, "userAgent");
    private LogRequestModel log2 = new LogRequestModel(2L, localDateTime, "ip2", "request", (short) 200, "userAgent");


    @BeforeEach
    void setUp() {
    }

    /**
     * LIST TEST CASES
     */

    @Test
    void givenLogsWhenListThenReturnLogs() {
        // given
        Page<LogRequestModel> pageModel = new PageImpl<>(Arrays.asList(log1, log2));
        given(repository.findAll(any(Pageable.class))).willReturn(pageModel);

        // when
        Page<LogRequestModel> result = service.list(pageModel.getPageable());

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
        given(repository.findById(log1.getId())).willReturn(Optional.of(log1));

        // when
        LogRequestModel result = service.findById(log1.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(log1.getId());
    }

    @Test
    void givenLogNotFoundWhenFindByIdLogThenThrowsNotFoundException() {
        // given
        Long id = 9999999999999L;
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

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
        given(repository.save(log1)).willReturn(log1);

        // when
        LogRequestModel result = service.save(log1);

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
        given(repository.findById(log1.getId())).willReturn(Optional.of(log1));
        given(repository.save(log1)).willReturn(log1);

        // when
        LogRequestModel result = service.update(log1.getId(), log1);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(log1.getId());
        verify(repository).findById(any());
        verify(repository).save(any());
    }

    @Test
    void givenLogNotFoundWhenEditLogThenThrowsNotFoundException() {
        // given
        given(repository.findById(log1.getId())).willReturn(Optional.empty());

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
        Page<LogRequestModel> pageModel = new PageImpl<>(Arrays.asList(log1, log2));
        given(repository.findAll(any(Specification.class), any(Pageable.class))).willReturn(pageModel);

        Specification<LogRequestModel> spec = new Specification<LogRequestModel>() {
            @Override
            public Predicate toPredicate(Root<LogRequestModel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return null;
            }
        };

        // when
        Page<LogRequestModel> result = service.searchByFilters(spec, pageModel.getPageable());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().contains(log1)).isTrue();
        assertThat(result.getContent().contains(log2)).isTrue();
    }


    /**
     * TestConfiguration guarantee this bean is only for test scope
     */
    @TestConfiguration
    static class LogRequestServiceTestContextConfiguration {
        @Bean
        LogRequestService logRequestService() {
            return new LogRequestServiceImpl();

        }


//        @Bean
//        Validator validator() {
//            return new LocalValidatorFactoryBean();
//        }
    }
}