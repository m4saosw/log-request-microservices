package br.com.massao.logrequest.infrastructure.config;

import br.com.massao.logrequest.domain.repository.DomainLogRequestRepositoryPort;
import br.com.massao.logrequest.domain.service.DomainLogRequestServiceAdapter;
import br.com.massao.logrequest.domain.service.LogRequestServicePort;
import br.com.massao.logrequest.infrastructure.repository.SpringDataJpaLogRequestRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = SpringDataJpaLogRequestRepository.class)
public class BeanConfiguration {

    /**
     * Registra manualmente LogRequestService como um bean do Spring
     * @param repository
     * @return
     */
    @Bean
    LogRequestServicePort logRequestService(final DomainLogRequestRepositoryPort repository) {
        return new DomainLogRequestServiceAdapter(repository);
    }
}
