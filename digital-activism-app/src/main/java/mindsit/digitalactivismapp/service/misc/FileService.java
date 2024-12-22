package mindsit.digitalactivismapp.service.misc;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<List<String>> uploadFiles(List<MultipartFile> multipartFiles, String directory) {
        List<String> fileNames = new ArrayList<>();

        Path path = get(DIRECTORY + directory);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                System.out.println("Could not create directory: " + path);
            }
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

        return ResponseEntity.ok(fileNames);
    }

    public ResponseEntity<Resource> downloadFile(String fileName, String directory) {
        Path filePath = getFilePath(fileName, directory);
        if (!Files.exists(filePath)) {
            System.out.println(fileName + " was not found");
            return ResponseEntity.ok(null);
        }
        try {
            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                    .headers(getFileHeaders(fileName)).body(resource);
        } catch (IOException e) {
            System.out.println("Could not download file: " + fileName);
            return ResponseEntity.ok(null);
        }
    }

    public ResponseEntity<Boolean> deleteFile(String fileName, String directory) {
        try {
            Path filePath = get(DIRECTORY + directory).toAbsolutePath().normalize().resolve(fileName);
            Files.delete(filePath);
            return ResponseEntity.ok(true);
        } catch (IOException e) {
            return ResponseEntity.ok(false);
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
