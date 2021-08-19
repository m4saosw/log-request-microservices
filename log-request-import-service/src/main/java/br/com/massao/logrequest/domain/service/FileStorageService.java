package br.com.massao.logrequest.domain.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


//@Service
public interface FileStorageService {

    /**
     * Save the file to a local directory
     *
     * @param fileName
     * @param file
     * @throws IOException
     */
    void save(String fileName, MultipartFile file) throws IOException;

    /**
     * Delete a file
     *
     * @param fileName
     */
    void delete(String fileName);

    /**
     * Return the full file path
     *
     * @param fileName
     * @return
     */
    String getFullFilesPath(String fileName);
}
