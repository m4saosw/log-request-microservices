package br.com.massao.logrequest.domain.service;

import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.domain.NotFoundException;

import java.util.List;

public interface LogRequestService {

    /**
     * List all logs
     *
     * @return
     */
    List<DomainLogRequest> list();


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
    DomainLogRequest save(DomainLogRequest model);


    /**
     * Update an existing log
     *
     * @param id
     * @param newLog
     * @return
     * @throws NotFoundException
     */
    DomainLogRequest update(Long id, DomainLogRequest newLog) throws NotFoundException;
}
