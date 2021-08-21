package br.com.massao.logrequest.domain.service;

import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.domain.NotFoundException;
import br.com.massao.logrequest.domain.repository.DomainLogRequestRepositoryPort;
import br.com.massao.logrequest.infrastructure.model.LogRequestModel;
import br.com.massao.logrequest.infrastructure.repository.LogRequestRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogRequestServiceImpl implements LogRequestService {
    @Autowired
    private LogRequestRepository repository;

    private DomainLogRequestRepositoryPort repositoryPort;

    public LogRequestServiceImpl(DomainLogRequestRepositoryPort repository) {
        this.repositoryPort = repository;
    }



    /**
     * List all logs
     *
     * @param pageable
     * @return
     */
    @Override
    @HystrixCommand(threadPoolKey = "largeQueryThreadPool")
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
    @HystrixCommand(threadPoolKey = "queryThreadPool")
    public DomainLogRequest findById(Long id) throws NotFoundException {
        log.debug("findById id={}", id);

        return repositoryPort.findById(id).orElseThrow(() -> new NotFoundException());
    }


    /**
     * Save a new log
     *
     * @param model
     * @return
     */
    @Override
    @HystrixCommand(threadPoolKey = "commandThreadPool")
    public LogRequestModel save(LogRequestModel model) {
        log.debug("save model={}", model);

        return repository.save(model);
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
    @HystrixCommand(threadPoolKey = "commandThreadPool")
    public LogRequestModel update(Long id, LogRequestModel newLog) throws NotFoundException {
        log.debug("update model={} {}", id, newLog);

        return repository.findById(id).map(model -> {
            model.setDate(newLog.getDate());
            model.setRequest(newLog.getRequest());
            model.setIp(newLog.getIp());
            model.setStatus(newLog.getStatus());
            model.setUserAgent(newLog.getUserAgent());

            return repository.save(model);
        }).orElseThrow(() -> new NotFoundException());
    }


    /**
     * Search logs by filters
     *
     * @param spec
     * @param pageable
     * @return
     */
    @Override
    @HystrixCommand(threadPoolKey = "largeQueryThreadPool")
    public Page<LogRequestModel> searchByFilters(Specification<LogRequestModel> spec, Pageable pageable) {
        log.debug("list pageable with filters");
        return repository.findAll(spec, pageable);
    }
}
