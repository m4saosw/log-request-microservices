package br.com.massao.logrequest.repository;

import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.domain.NotFoundException;
import br.com.massao.logrequest.model.LogRequestModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class JpaLogRequestRepositoryTest {

    @Mock
    SpringDataJpaLogRequestRepository springRepository;

    @InjectMocks
    JpaLogRequestRepository repository;

    DomainLogRequest domain1;
    DomainLogRequest domain2;
    DomainLogRequest domain3;


    @BeforeEach
    void setUp() {
        //springRepository.deleteAll();

        domain1 = DomainLogRequest.builder()
                .date(LocalDateTime.of(2021, 07, 24, 22, 05, 10, 020))
                .request("request1")
                .ip("ip1")
                .status((short) 201)
                .userAgent("userAgent1")
                .build();

        domain2 = DomainLogRequest.builder()
                .date(LocalDateTime.of(2021, 07, 25, 23, 10, 00, 000))
                .request("request2")
                .ip("ip2")
                .status((short) 201)
                .userAgent("userAgent2")
                .build();

        domain3 = DomainLogRequest.builder()
                .date(LocalDateTime.of(2021, 07, 26, 23, 10, 00, 000))
                .request("request3")
                .ip("ip3")
                .status((short) 201)
                .userAgent("userAgent3")
                .build();
    }


    /**
     * CREATE TEST CASES
     */

    @Test
    void givenLogWhenCreateThenSaveAndReturnLog() {
        // given
        LogRequestModel model1 = new LogRequestModel(domain1);
        when(springRepository.save(any())).thenReturn(model1);

        // when
        DomainLogRequest result = repository.create(domain1);

        // then
        assertThat(result.getRequest()).isEqualTo(domain1.getRequest());
    }


    @Test
    void givenInvalidValidationLogWhenCreateThenReturnLog() {
        // given
        when(springRepository.save(any())).thenThrow(ConstraintViolationException.class);

        // when/then
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(
                () -> repository.create(domain1));
    }


    /**
     * FIND BY ID TEST CASES
     */

    @Test
    void givenExistingLogIdWhenFindByIdThenReturnLog() {
        // given
        LogRequestModel model1 = new LogRequestModel(domain1);
        model1.setId(1L);

        when(springRepository.findById(anyLong())).thenReturn(Optional.of(model1));

        // when
        Optional<DomainLogRequest> resultFindById = repository.findById(model1.getId());

        // then
        assertThat(resultFindById.get().getRequest()).isEqualTo(domain1.getRequest());
    }


    /**
     * EDIT TEST CASES
     */

    @Test
    void givenExistingLogWhenEditThenSaveAndReturnLog() throws NotFoundException {
        // given
        domain1.setRequest(domain3.getRequest());
        LogRequestModel model1 = new LogRequestModel(domain1);
        model1.setId(1L);

        when(springRepository.findById(anyLong())).thenReturn(Optional.of(model1));
        when(springRepository.save(any())).thenReturn(model1);


        // when
        //DomainLogRequest result = repository.save(domain1);
        DomainLogRequest result = repository.update(model1.getId(), domain1);

        // then
        assertThat(result.getRequest()).isEqualTo(domain3.getRequest());
    }


    /**
     * LIST TEST CASES
     */

    @Test
    void givenLogsWhenListThenReturnLogs() {
        // given
        LogRequestModel model1 = new LogRequestModel(domain1);
        LogRequestModel model2 = new LogRequestModel(domain2);
        LogRequestModel model3 = new LogRequestModel(domain3);
        when(springRepository.findAll()).thenReturn(Arrays.asList(model1, model2, model3));

        // when
        List<DomainLogRequest> results = repository.listAll();

        // then
        assertThat(results.get(0).getRequest()).isEqualTo(domain1.getRequest());
        assertThat(results.get(1).getRequest()).isEqualTo(domain2.getRequest());
        assertThat(results.get(2).getRequest()).isEqualTo(domain3.getRequest());
    }
}