package br.com.massao.logrequest.domain.service;

import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.domain.NotFoundException;
import br.com.massao.logrequest.infrastructure.model.LogRequestModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *  Tambem chamado de Adapter na hexagonal architecture.
 *  Representa um Adaptador para uma Porta
 */
@Service
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
     * @param model
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
     * @param spec
     * @param pageable
     * @return
     */
    Page<LogRequestModel> searchByFilters(Specification<LogRequestModel> spec, Pageable pageable);
}
