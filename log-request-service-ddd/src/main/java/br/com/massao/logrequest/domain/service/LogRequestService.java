package br.com.massao.logrequest.domain.service;

import br.com.massao.logrequest.application.resource.LogRequestParams;
import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.domain.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Porta do Servico de Dominio
 * Port na hexagonal architecture.
 */
public interface LogRequestService {

    /**
     * List all logs
     *
     * @param pageable
     * @return
     */
    Page<DomainLogRequest> list(Pageable pageable);


    /**
     * Find a log by id
     *
     * @param id
     * @return
     * @throws NotFoundException
     */
    DomainLogRequest findById(Long id) throws NotFoundException;


    /**
     * Save a new log
     *
     * @param domain
     * @return
     */
    DomainLogRequest save(DomainLogRequest domain);


    /**
     * Update an existing log
     *
     * @param id
     * @param newLog
     * @return
     * @throws NotFoundException
     */
    DomainLogRequest update(Long id, DomainLogRequest newLog) throws NotFoundException;


    /**
     * Search logs by filters
     *
     * @param parameters
     * @param pageable
     * @return
     */
    Page<DomainLogRequest> searchByFilters(LogRequestParams parameters, Pageable pageable);
}
