package br.com.massao.logrequest.domain.service;

import br.com.massao.logrequest.application.util.FilesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${files.path.import}")
    private String filesPath;

    @Autowired
    private FilesUtil filesUtil;

    /**
     * Save the file to a local directory
     *
     * @param fileName
     * @param file
     * @throws IOException
     */
    @Override
    public void save(String fileName, MultipartFile file) throws IOException {
        log.debug("Saving new file {}", fileName);

        filesUtil.save(getFullFilesPath(fileName), file);
    }

    /**
     * Delete a file
     *
     * @param fileName
     */
    @Override
    public void delete(String fileName) {
        log.debug("Deleting file {}", fileName);

        filesUtil.delete(getFullFilesPath(fileName));
    }

    /**
     * Return the full file path
     *
     * @param fileName
     * @return
     */
    @Override
    public String getFullFilesPath(String fileName) {
        return filesPath + "\\" + fileName;
    }
}
