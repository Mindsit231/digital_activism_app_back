package mindsit.digitalactivismapp.service.misc;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;

@Service
public class FileService {
    public static final String DIRECTORY = "src/main/resources/uploads/";

    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String directory) throws IOException {
        List<String> fileNames = new ArrayList<>();

        Path path = get(DIRECTORY + directory);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        for (MultipartFile file : multipartFiles) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            Path fileStorage = get(DIRECTORY + directory + "/" + fileName).toAbsolutePath().normalize();

            try {
                copy(file.getInputStream(), fileStorage, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Copying file: " + fileName + " to " + fileStorage);
            } catch (IOException e) {
                System.out.println("Could not store file " + fileName + ". Please try again!");
            }

            fileNames.add(fileName);
        }

        return fileNames;
    }

    public UrlResource downloadFile(String fileName, String directory) throws IOException {
        Path filePath = get(DIRECTORY + directory).toAbsolutePath().normalize().resolve(fileName);
        if (!Files.exists(filePath)) {
            System.out.println(fileName + " was not found");
        }
        return new UrlResource(filePath.toUri());
    }

    public boolean deleteFile(String fileName, String directory) throws IOException {
        try {
            Path filePath = get(DIRECTORY + directory).toAbsolutePath().normalize().resolve(fileName);
            Files.delete(filePath);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public HttpHeaders getFileHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("File-Name", fileName);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name=" + fileName);
        return headers;
    }

    public Path getFilePath(String fileName, String directory) {
        return get(DIRECTORY + directory).toAbsolutePath().normalize().resolve(fileName);
    }
}
