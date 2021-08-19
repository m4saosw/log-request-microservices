package br.com.massao.logrequest.application.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Component
public class FilesUtil {

    public void save(String fullFilePath, MultipartFile file) throws IOException {
        log.debug("Saving file <{}>", fullFilePath);

        FileOutputStream stream = new FileOutputStream(fullFilePath);
        stream.write(file.getBytes());
        stream.close();
    }


    public void delete(String fullFilePath) {
        try {
            Files.deleteIfExists(Paths.get(fullFilePath));
        } catch (IOException e) {
            log.warn("File to delete not found <{}>", fullFilePath);
        }
    }

}
