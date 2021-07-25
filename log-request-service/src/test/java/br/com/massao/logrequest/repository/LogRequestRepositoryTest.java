package br.com.massao.logrequest.repository;

import br.com.massao.logrequest.model.LogRequestModel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class LogRequestRepositoryTest {
    @Autowired
    LogRequestRepository repository;

    LogRequestModel model1;
    LogRequestModel model2;
    LogRequestModel model3;


    @BeforeEach
    void setUp() {
        repository.deleteAll();

        model1 = LogRequestModel.builder()
                .date(LocalDateTime.of(2021, 07, 24, 22, 05, 10, 020))
                .request("request1")
                .ip("ip1")
                .status((short) 201)
                .userAgent("userAgent1")
                .build();

        model2 = LogRequestModel.builder()
                .date(LocalDateTime.of(2021, 07, 25, 23, 10, 00, 000))
                .request("request2")
                .ip("ip2")
                .status((short) 201)
                .userAgent("userAgent2")
                .build();

        model3 = LogRequestModel.builder()
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
        // when
        LogRequestModel result = repository.save(model1);

        // then
        assertThat(result.getRequest()).isEqualTo(model1.getRequest());
    }


    @Test
    void givenInvalidValidationLogWhenCreateThenReturnLog() {
        model1.setRequest(null);

        // when/then
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(
                () -> repository.save(model1));
    }


    /**
     * FIND BY ID TEST CASES
     */

    @Test
    void givenExistingLogIdWhenFindByIdThenReturnLog() {
        repository.save(model1);
        repository.save(model2);

        // when
        Optional<LogRequestModel> resultFindById = repository.findById(model1.getId());

        // then
        assertThat(resultFindById.get().getRequest()).isEqualTo(model1.getRequest());
    }


    /**
     * EDIT TEST CASES
     */

    @Test
    void givenExistingLogWhenEditThenSaveAndReturnLog() {
        model1.setRequest(model3.getRequest());

        LogRequestModel result = repository.save(model1);

        assertThat(result.getRequest()).isEqualTo(model3.getRequest());
    }


    /**
     * LIST TEST CASES
     */

    @Test
    void givenLogsWhenListThenReturnLogs() {
        repository.save(model1);
        repository.save(model2);
        repository.save(model3);

        // when
        List<LogRequestModel> results = repository.findAll();

        // then
        assertThat(results.get(0).getRequest()).isEqualTo(model1.getRequest());
        assertThat(results.get(1).getRequest()).isEqualTo(model2.getRequest());
        assertThat(results.get(2).getRequest()).isEqualTo(model3.getRequest());
    }
}