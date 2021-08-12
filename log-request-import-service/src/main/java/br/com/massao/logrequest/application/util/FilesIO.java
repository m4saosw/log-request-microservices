package br.com.massao.logrequest.application.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesIO {

    public static void save(String path, String fileName, MultipartFile file) throws IOException {
        File newFile = new File(path + fileName);


        Files.createDirectories(Paths.get(path));
        FileOutputStream stream = new FileOutputStream(newFile);
        stream.write(file.getBytes());
        stream.close();

    }


}
