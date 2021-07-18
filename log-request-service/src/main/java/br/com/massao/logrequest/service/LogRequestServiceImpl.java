package br.com.massao.logrequest.service;

import br.com.massao.logrequest.exception.NotFoundException;
import br.com.massao.logrequest.model.LogRequestModel;
import br.com.massao.logrequest.repository.LogRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogRequestServiceImpl implements LogRequestService {
    @Autowired
    private LogRequestRepository repository;


    /**
     * List all logs
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<LogRequestModel> list(Pageable pageable) {
        log.debug("list pageable");

        return repository.findAll(pageable);
    }


    /**
     * Find a log by id
     *
     * @param id
     * @return
     * @throws NotFoundException
     */
    @Override
    public LogRequestModel findById(Long id) throws NotFoundException {
        log.debug("findById id={}", id);

        return repository.findById(id).orElseThrow(() -> new NotFoundException());
    }


    /**
     * Save a new log
     * @param model
     * @return
     */
    public LogRequestModel save(LogRequestModel model) {
        log.debug("save model={}", model);

        return repository.save(model);
    }
}
