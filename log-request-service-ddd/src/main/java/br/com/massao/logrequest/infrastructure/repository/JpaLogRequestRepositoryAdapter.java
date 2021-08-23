package br.com.massao.logrequest.infrastructure.repository;

import br.com.massao.logrequest.domain.DomainLogRequest;
import br.com.massao.logrequest.domain.DomainLogRequestParams;
import br.com.massao.logrequest.domain.NotFoundException;
import br.com.massao.logrequest.domain.repository.DomainLogRequestRepositoryPort;
import br.com.massao.logrequest.infrastructure.model.LogRequestModel;
import br.com.massao.logrequest.infrastructure.repository.query.LogRequestSpecifications;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JpaLogRequestRepositoryAdapter implements DomainLogRequestRepositoryPort {
    private final SpringDataJpaLogRequestRepository repository;

    @Autowired
    public JpaLogRequestRepositoryAdapter(SpringDataJpaLogRequestRepository repository) {
        this.repository = repository;
    }


    @Override
    @HystrixCommand(threadPoolKey = "largeQueryThreadPool")
    public Page<DomainLogRequest> listAll(Pageable page) {
        Page<LogRequestModel> modelsPage = repository.findAll(page);
        return modelsPage.map(LogRequestModel::toDomain);
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
    public Optional<DomainLogRequest> findById(Long id) {

        Optional<LogRequestModel> model = repository.findById(id);
        if (model.isPresent()) {
            return Optional.of(model.get().toDomain());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Update an existing log
     *
     * @param editedLog
     * @return
     * @throws NotFoundException
     */
    @Override
    @HystrixCommand(threadPoolKey = "commandThreadPool")
    public DomainLogRequest update(Long id, DomainLogRequest editedLog) throws NotFoundException {

        return repository.findById(id).map(model -> {
            model.setDate(editedLog.getDate());
            model.setRequest(editedLog.getRequest());
            model.setIp(editedLog.getIp());
            model.setStatus(editedLog.getStatus());
            model.setUserAgent(editedLog.getUserAgent());

            LogRequestModel saved = repository.save(model);

            return saved.toDomain();
        }).orElseThrow(() -> new NotFoundException());
    }


    /**
     * Save a new log
     *
     * @param log
     * @return
     */
    @Override
    @HystrixCommand(threadPoolKey = "commandThreadPool")
    public DomainLogRequest create(DomainLogRequest log) {
        final LogRequestModel model = new LogRequestModel(log);

        return repository.save(model).toDomain();
    }


    /**
     * List logs by filters
     *
     * @param parameters
     * @param page
     * @return
     */
    @Override
    public Page<DomainLogRequest> listAll(DomainLogRequestParams parameters, Pageable page) {
        final Specification<LogRequestModel> specification = new LogRequestSpecifications().fromParams(parameters);
        final Page<LogRequestModel> modelsPage = repository.findAll(specification, page);

        return modelsPage.map(LogRequestModel::toDomain);
    }
}
