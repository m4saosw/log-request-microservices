package br.com.massao.logrequest.infrastructure.repository;

import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.domain.NotFoundException;
import br.com.massao.logrequest.domain.repository.DomainLogRequestRepositoryPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@SpringJUnitConfig
@SpringBootTest
class JpaLogRequestRepositoryIntegrationTest {
//    @Autowired
//    JpaLogRequestRepository repository;

    @Autowired
    SpringDataJpaLogRequestRepository springRepository;

    @Autowired
    DomainLogRequestRepositoryPort repository;

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

    @AfterEach
    void cleanUp() {
        springRepository.deleteAll();
    }


    /**
     * CREATE TEST CASES
     */

    @Test
    void givenLogWhenCreateThenSaveAndReturnLog() {
        // when
        //DomainLogRequest result = repository.create(domain1);
        DomainLogRequest result = repository.create(domain1);

        // then
        assertThat(result.getRequest()).isEqualTo(domain1.getRequest());
    }


    @Test
    void givenInvalidValidationLogWhenCreateThenReturnLog() {
        domain1.setRequest(null);

        // when/then
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(
                () -> repository.create(domain1));
    }


    /**
     * FIND BY ID TEST CASES
     */

    @Test
    void givenExistingLogIdWhenFindByIdThenReturnLog() {
        DomainLogRequest persisted1 = repository.create(domain1);
        repository.create(domain2);

        // when
        Optional<DomainLogRequest> resultFindById = repository.findById(persisted1.getId());

        // then
        assertThat(resultFindById.get().getRequest()).isEqualTo(domain1.getRequest());
    }


    /**
     * EDIT TEST CASES
     */

    @Test
    void givenExistingLogWhenEditThenSaveAndReturnLog() throws NotFoundException {
        DomainLogRequest persisted1 = repository.create(domain1);

        domain1.setRequest(domain3.getRequest());

        //DomainLogRequest result = repository.save(domain1);
        DomainLogRequest result = repository.update(persisted1.getId(), domain1);

        assertThat(result.getRequest()).isEqualTo(domain3.getRequest());
    }


    /**
     * LIST TEST CASES
     */

    @Test
    void givenLogsWhenListThenReturnLogs() {
        repository.create(domain1);
        repository.create(domain2);
        repository.create(domain3);

        // when
        List<DomainLogRequest> results = repository.listAll();

        // then
        assertThat(results.get(0).getRequest()).isEqualTo(domain1.getRequest());
        assertThat(results.get(1).getRequest()).isEqualTo(domain2.getRequest());
        assertThat(results.get(2).getRequest()).isEqualTo(domain3.getRequest());
    }
}