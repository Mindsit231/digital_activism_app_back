package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.model.MyEntity;
import mindsit.digitalactivismapp.service.EntityService;
import mindsit.digitalactivismapp.service.misc.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static mindsit.digitalactivismapp.controller.AuthenticationController.AUTHORIZATION_HEADER;

public abstract class EntityController<T extends MyEntity, S extends EntityService<T, ?>> {
    protected final S entityService;
    protected final Class<T> entityClass;
    protected final FileService fileService = new FileService();

    protected EntityController(S entityService, Class<T> entityClass) {
        this.entityService = entityService;
        this.entityClass = entityClass;
    }

    public ResponseEntity<List<String>> uploadFiles(
            @RequestHeader(AUTHORIZATION_HEADER) String authHeader,
            @RequestParam("files") List<MultipartFile> multipartFiles)
            throws IOException {
        return ResponseEntity.ok(fileService.uploadFiles(multipartFiles, entityClass.getSimpleName().toLowerCase()));
    }

    public ResponseEntity<Resource> downloadFile(
            @RequestHeader(AUTHORIZATION_HEADER) String authHeader,
            @PathVariable("fileName") String fileName) throws IOException {
        Resource resource = fileService.downloadFile(fileName, entityClass.getSimpleName().toLowerCase());
        Path filePath = fileService.getFilePath(fileName, entityClass.getSimpleName().toLowerCase());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(fileService.getFileHeaders(fileName)).body(resource);
    }

    public ResponseEntity<Boolean> deleteFile(
            @RequestHeader(AUTHORIZATION_HEADER) String authHeader,
            @PathVariable("fileName") String fileName) throws IOException {
        return ResponseEntity.ok(fileService.deleteFile(fileName, entityClass.getSimpleName().toLowerCase()));
    }

}
