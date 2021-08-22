package br.com.massao.logrequest.infrastructure.config;

import br.com.massao.logrequest.domain.repository.DomainLogRequestRepositoryPort;
import br.com.massao.logrequest.domain.service.DomainLogRequestServiceAdapter;
import br.com.massao.logrequest.domain.service.LogRequestService;
import br.com.massao.logrequest.infrastructure.repository.LogRequestRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = LogRequestRepository.class)
public class BeanConfiguration {

    /**
     * Registra manualmente LogRequestService como um bean do Spring
     * @param repository
     * @return
     */
    @Bean
    LogRequestService logRequestService(final DomainLogRequestRepositoryPort repository) {
        return new DomainLogRequestServiceAdapter(repository);
    }
}
