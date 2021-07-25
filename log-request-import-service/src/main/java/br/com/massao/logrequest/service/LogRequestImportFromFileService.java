package br.com.massao.logrequest.service;

import br.com.massao.logrequest.model.LogRequestModel;
import br.com.massao.logrequest.repository.LogRequestRepository;
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
public class LogRequestImportFromFileService {
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

    //@Override
    public List<LogRequestModel> saveMany(@Valid List<LogRequestModel> models) {
        log.debug("save many models ={}", models);

        // TODO - refactor - handler exception
        for (LogRequestModel model : models) {
            Set<ConstraintViolation<LogRequestModel>> violations = validator.validate(model);
            if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
        }
        return repository.saveAll(models); // transacional
    }


    //@Override
    public JobExecution runBatch(Long now, String fileName) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        Map<String, JobParameter> params = new HashMap<>();
        params.put("time", new JobParameter(now));
        params.put("pathFile", new JobParameter(storageService.getFullFilesPath(fileName)));

        return jobLauncher.run(job, new JobParameters(params));
    }


    //@Override
    public JobExecution jobs(Long jobId) throws NoSuchJobException {

        return Optional.ofNullable(explorer.getJobExecution(jobId)).orElseThrow(() -> new NoSuchJobException("Job Id not found"));
    }
}
