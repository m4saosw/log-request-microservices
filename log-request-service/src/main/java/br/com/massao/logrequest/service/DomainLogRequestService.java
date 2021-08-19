package br.com.massao.logrequest.service;

import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.domain.NotFoundException;
import br.com.massao.logrequest.domain.repository.DomainLogRequestRepositoryPort;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DomainLogRequestService implements LogRequestService {
    private final DomainLogRequestRepositoryPort repository;

    public DomainLogRequestService(DomainLogRequestRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * List all logs
     *
     * @return
     */
    @Override
    public List<DomainLogRequest> list() {
        log.debug("list");

        return repository.listAll();
    }


    /**
     * Find a log by id
     *
     * @param id
     * @return
     * @throws NotFoundException
     */
    @Override
    public DomainLogRequest findById(Long id) throws NotFoundException {
        log.debug("findById id={}", id);

        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }


    /**
     * Save a new log
     *
     * @param domain
     * @return
     */
    @Override
    public DomainLogRequest save(DomainLogRequest domain) {
        log.debug("save domain={}", domain);

        return repository.create(domain);
    }


    /**
     * Update an existing log
     *
     * @param id
     * @param newLog
     * @return
     * @throws NotFoundException
     */
    @Override
    public DomainLogRequest update(Long id, DomainLogRequest newLog) throws NotFoundException {
        log.debug("update model={} {}", id, newLog);

        return repository.update(id, newLog);
    }


}
