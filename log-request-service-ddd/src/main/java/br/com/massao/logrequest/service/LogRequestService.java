package br.com.massao.logrequest.service;

import br.com.massao.logrequest.application.exception.NotFoundException;
import br.com.massao.logrequest.infrastructure.model.LogRequestModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public interface LogRequestService {

    /**
     * List all logs
     *
     * @param pageable
     * @return
     */
    Page<LogRequestModel> list(Pageable pageable);


    /**
     * Find a log by id
     *
     * @param id
     * @return
     * @throws NotFoundException
     */
    LogRequestModel findById(Long id) throws NotFoundException;


    /**
     * Save a new log
     *
     * @param model
     * @return
     */
    LogRequestModel save(LogRequestModel model);


    /**
     * Update an existing log
     *
     * @param id
     * @param newLog
     * @return
     * @throws NotFoundException
     */
    LogRequestModel update(Long id, LogRequestModel newLog) throws NotFoundException;


    /**
     * Search logs by filters
     *
     * @param spec
     * @param pageable
     * @return
     */
    Page<LogRequestModel> searchByFilters(Specification<LogRequestModel> spec, Pageable pageable);
}
