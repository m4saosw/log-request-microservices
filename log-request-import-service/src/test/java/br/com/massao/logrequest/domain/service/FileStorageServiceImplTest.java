package br.com.massao.logrequest.domain.service;

import br.com.massao.logrequest.application.util.FilesUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class FileStorageServiceImplTest {
    @Autowired
    private FileStorageService service;

    @MockBean
    private FilesUtil filesUtil;

    @BeforeEach
    void setUp() {
    }


    /**
     * SAVE TEST CASES
     */

    @Test
    void givenFilenameAndFileWhenSaveThenSaveSuccess() throws IOException {
        // given
        String filename = "filename.csv";
        String content1 = "2019-01-01 14:00:43.128|200.235.3.29|\"POST / HTTP/1.1\"|200|\"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0\"";
        MockMultipartFile file = new MockMultipartFile("file", "filename.csv", "application/vnd.ms-excel", content1.getBytes());

        // when
        service.save(filename, file);

        // then
        verify(filesUtil).save(anyString(), any(MultipartFile.class));
    }


    @Test
    void givenErrorOnSaveSaveThenThrowsIOException() throws IOException {
        // given
        String content1 = "2019-01-01 14:00:43.128|200.235.3.29|\"POST / HTTP/1.1\"|200|\"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0\"";
        MockMultipartFile file = new MockMultipartFile("file", "filename.csv", "application/vnd.ms-excel", content1.getBytes());

        doThrow(IOException.class).when(filesUtil).save(any(String.class), any(MultipartFile.class));

        // when/then
        Assertions.assertThatExceptionOfType(IOException.class).isThrownBy(
                () -> service.save("filename1.csv", file));
    }


    @Test
    void getFullFilesPath() {
        // given
        final String filename = "filename1.csv";

        //when
        String fullFilesPath = service.getFullFilesPath(filename);

        // then
        assertNotNull(fullFilesPath);
        assertTrue(fullFilesPath.length() > filename.length());
    }


    /**
     * TestConfiguration guarantee this bean is only for test scope
     */
    @TestConfiguration
    static class FileStorageServiceTestContextConfiguration {
        @Bean
        FileStorageService fileStorageService() {
            return new FileStorageServiceImpl();
        }
    }
}