package br.com.massao.logrequest.domain.service;

import br.com.massao.logrequest.infrastructure.model.LogRequestModel;
import br.com.massao.logrequest.infrastructure.repository.LogRequestRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.*;

@Slf4j
@Service
@Validated
public class LogRequestImportFromFileServiceImpl implements LogRequestImportFromFileService {
    @Autowired
    private LogRequestRepository repository;

    @Autowired
    private Validator validator;

    @Autowired
    @Qualifier("myJobLauncher")
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @Autowired
    JobLocator jobLocator;

    @Autowired
    JobExplorer explorer;

    @Autowired
    FileStorageService storageService;


    /**
     * Save all logs to repository
     *
     * @param models
     * @return
     */
    @Override
    @HystrixCommand(threadPoolKey = "commandThreadPool")
    public List<LogRequestModel> saveMany(@Valid List<LogRequestModel> models) {
        log.debug("save many models ={}", models);

        // TODO - refactor - handler exception
        for (LogRequestModel model : models) {
            Set<ConstraintViolation<LogRequestModel>> violations = validator.validate(model);
            if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
        }
        return repository.saveAll(models); // transacional
    }

    /**
     * Run a batch job in async mode
     *
     * @param now
     * @param fileName
     * @return
     * @throws JobExecutionAlreadyRunningException
     * @throws JobRestartException
     * @throws JobInstanceAlreadyCompleteException
     * @throws JobParametersInvalidException
     */
    @Override
    @HystrixCommand(threadPoolKey = "commandThreadPool")
    public JobExecution runBatch(Long now, String fileName) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        Map<String, JobParameter> params = new HashMap<>();
        params.put("time", new JobParameter(now));
        params.put("pathFile", new JobParameter(storageService.getFullFilesPath(fileName)));

        return jobLauncher.run(job, new JobParameters(params));
    }


    /**
     * Get status of job execution by job id
     *
     * @param jobId
     * @return
     * @throws NoSuchJobException
     */
    @Override
    @HystrixCommand(threadPoolKey = "queryThreadPool")
    public JobExecution jobs(Long jobId) throws NoSuchJobException {

        return Optional.ofNullable(explorer.getJobExecution(jobId)).orElseThrow(() -> new NoSuchJobException("Job Id not found"));
    }
}
