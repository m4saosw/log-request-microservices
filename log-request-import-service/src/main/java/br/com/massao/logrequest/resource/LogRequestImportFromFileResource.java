package br.com.massao.logrequest.resource;

import br.com.massao.logrequest.dto.JobExecutionOutput;
import br.com.massao.logrequest.dto.JobExecutionResource;
import br.com.massao.logrequest.exception.ApiError;
import br.com.massao.logrequest.service.FileStorageService;
import br.com.massao.logrequest.service.LogRequestImportFromFileService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@RestController
@RequestMapping("v1/import-from-file")
public class LogRequestImportFromFileResource {
    @Autowired
    private LogRequestImportFromFileService service;

    @Autowired
    private FileStorageService storageService;


    /**
     * Import many log requests from a given file (in csv format)
     *
     * @param file
     * @return
     * @throws IOException
     * @throws JobInstanceAlreadyCompleteException
     * @throws JobExecutionAlreadyRunningException
     * @throws JobParametersInvalidException
     * @throws JobRestartException
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Import many log requests from a given file by asynchronous processing. The processing status can be get by status endpoint in the response payload.", notes = "Specifications required for file: csv format; delimiter |; fields: Date | IP | Request | Status | User Agent; date format: yyyy-MM-dd HH:mm:ss.SSS")

    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 400, message = "Bad Request", response = ApiError.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.ACCEPTED) // This removes http 200 on Swagger
    public ResponseEntity<JobExecutionResource> importFromFileAsyncMode(@RequestPart(value = "file") MultipartFile file) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        log.info("importFromFileAsyncMode file={}", file.getName());

        final Long now = System.currentTimeMillis();
        final String fileName = getFileName(now);

        // Save the file
        storageService.save(fileName, file);

        // Run Job in async mode
        JobExecution jobExecution = service.runBatch(now, fileName);

        // Create the response payload
        JobExecutionOutput output = JobExecutionOutput.fromJobExecution(jobExecution);
        JobExecutionResource jobExecutionResource = new JobExecutionResource(output);

        return new ResponseEntity<>(jobExecutionResource, HttpStatus.ACCEPTED);
    }


    /**
     * Get the status of an imported file by job id
     *
     * @param jobId
     * @return
     * @throws NoSuchJobException
     */
    @GetMapping(value = "/status/{jobId}")
    @ApiOperation(value = "Get status for a job execution by job id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not Found", response = ApiError.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ApiError.class)
    })
    public ResponseEntity<JobExecutionOutput> status(@PathVariable(value = "jobId") Long jobId) throws NoSuchJobException {
        log.info("status jobId={}", jobId);

        JobExecution execution = service.jobs(jobId);

        return ResponseEntity.ok(JobExecutionOutput.fromJobExecution(execution));
    }


    /**
     * Return an internal filename to persisting it
     *
     * @param now
     * @return
     */
    private String getFileName(Long now) {
        return now + ".csv";
    }
}
