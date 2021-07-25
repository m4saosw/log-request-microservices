package br.com.massao.logrequest.resource;

import br.com.massao.logrequest.service.FileStorageService;
import br.com.massao.logrequest.service.LogRequestImportFromFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest({LogRequestImportFromFileResource.class})  //  Auto-configure the Spring MVC infrastructure for unit tests
class LogRequestImportFromFileResourceTest {
    public static final String URL_IMPORT = "/v1/import-from-file";
    public static final String URL_STATUS = "/v1/import-from-file/status/{id}";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LogRequestImportFromFileService service;

    @MockBean
    private FileStorageService storageService;

    @BeforeEach
    void setUp() {
    }


    /**
     * IMPORT TEST CASES
     */

    //@Test
    void givenFileOKWhenImportFromFileAsyncModeThenSaveAndReturnStatus202() throws Exception {
        // given
        String content1 = "2019-01-01 14:00:43.128|200.235.3.29|\"POST / HTTP/1.1\"|200|\"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0\"";
        MockMultipartFile file = new MockMultipartFile("file", "filename.csv", "application/vnd.ms-excel", content1.getBytes());
        JobExecution jobExecution = null;
        // given(storageService.save(anyString(), any(MultipartFile.class)))
        given(service.runBatch(anyLong(), anyString())).willReturn(jobExecution);

        // when/ then
        mvc.perform(multipart(URL_IMPORT).file("file", file.getBytes()).characterEncoding("UTF-8")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenFileNotAttachedWhenImportFromFileAsyncModeThenReturnStatus400() throws Exception {
        // given

        // when/ then
        mvc.perform(post(URL_IMPORT)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    //@Test
    void givenErrorOnSavingFileWhenImportFromFileAsyncModeThenReturnStatus500() throws Exception {
        // given
        JobExecution jobExecution = null;
        doThrow(IOException.class).when(storageService).save(any(String.class), any(MultipartFile.class));
        given(service.runBatch(anyLong(), anyString())).willReturn(jobExecution);

        // when/ then
        mvc.perform(post(URL_IMPORT)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isInternalServerError());
    }


    /**
     * STATUS TEST CASES
     */

    @Test
    void givenJobExecutionWhenStatusThenReturnJobExecutionWithStatus200() throws Exception {
        // given
        JobExecution jobExecution = new JobExecution(new JobInstance(1L, "jobName1"), 1L, null, null);
        given(service.jobs(anyLong())).willReturn(jobExecution);

        // when/then
        mvc.perform(get(URL_STATUS, jobExecution.getJobId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.jobId").value(jobExecution.getId()));

    }

    @Test
    void givenJobExecutionNotFoundWhenStatusThenReturnStatus404() throws Exception {
        // given
        given(service.jobs(anyLong())).willThrow(NoSuchJobException.class);

        // when/then
        mvc.perform(get(URL_STATUS, 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenInternalErrorWhenStatusThenReturnStatus500() throws Exception {
        // given
        given(service.jobs(anyLong())).willThrow(NullPointerException.class);

        // when/then
        mvc.perform(get(URL_STATUS, 1L))
                .andExpect(status().isInternalServerError());
    }
}