package br.com.massao.logrequest.service;

import br.com.massao.logrequest.model.LogRequestModel;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

//@Service
@Validated
public interface LogRequestImportFromFileService {

    /**
     * Save all logs to repository
     *
     * @param models
     * @return
     */
    List<LogRequestModel> saveMany(@Valid List<LogRequestModel> models);

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
    JobExecution runBatch(Long now, String fileName) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException;


    /**
     * Get status of job execution by job id
     *
     * @param jobId
     * @return
     * @throws NoSuchJobException
     */
    JobExecution jobs(Long jobId) throws NoSuchJobException;
}
