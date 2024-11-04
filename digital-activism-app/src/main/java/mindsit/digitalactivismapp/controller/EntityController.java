package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.model.MyEntity;
import mindsit.digitalactivismapp.model.misc.PostBody;
import mindsit.digitalactivismapp.service.EntityService;
import mindsit.digitalactivismapp.service.misc.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class EntityController<T extends MyEntity, S extends EntityService<T, ?>> {
    protected final S entityService;
    protected final Class<T> entityClass;
    protected final FileService fileService = new FileService();

    protected EntityController(S entityService, Class<T> entityClass) {
        this.entityService = entityService;
        this.entityClass = entityClass;
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<T>> getAllEntities() {
        return ResponseEntity.ok(entityService.findAllEntities());
    }

    @PostMapping("/public/all-limited")
    public ResponseEntity<List<T>> getAllLimitedEntities(@RequestBody PostBody postBody) {
        return ResponseEntity.ok(entityService.findAllLimitedEntities(postBody.getExcludeIds(), postBody.getLimit()));
    }

    @GetMapping("/user/find-by-id/{id}")
    public ResponseEntity<T> findEntityById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(entityService.findEntityById(id));
    }

    @PostMapping("/public/add")
    public ResponseEntity<T> addEntity(@RequestBody T entity) {
        return new ResponseEntity<>(entityService.addEntity(entity), HttpStatus.CREATED);
    }

    @PutMapping("/user/update")
    public ResponseEntity<T> updateEntity(@RequestBody T entity) {
        return ResponseEntity.ok(entityService.updateEntity(entity));
    }

    @GetMapping("/user/delete/{id}")
    public ResponseEntity<Long> deleteEntity(@PathVariable("id") Long id) {
        entityService.deleteEntityById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user/upload-files")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles)
            throws IOException {
        return ResponseEntity.ok(fileService.uploadFiles(multipartFiles, entityClass.getSimpleName().toLowerCase()));
    }

    @GetMapping("/user/download-file/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) throws IOException {
        Resource resource = fileService.downloadFile(fileName, entityClass.getSimpleName().toLowerCase());
        Path filePath = fileService.getFilePath(fileName, entityClass.getSimpleName().toLowerCase());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(fileService.getFileHeaders(fileName)).body(resource);
    }

    @GetMapping("/user/delete-file/{fileName}")
    public ResponseEntity<Boolean> deleteFile(@PathVariable("fileName") String fileName) throws IOException {
        return ResponseEntity.ok(fileService.deleteFile(fileName, entityClass.getSimpleName().toLowerCase()));
    }

}
