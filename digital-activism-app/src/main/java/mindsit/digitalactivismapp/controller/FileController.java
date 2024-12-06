package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.modelDTO.FileRequest;
import mindsit.digitalactivismapp.service.misc.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
public class FileController {
    protected final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/authenticated/file/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles,
                                                    @RequestParam String entityName)
            throws IOException {
        return ResponseEntity.ok(fileService.uploadFiles(multipartFiles, entityName));
    }

    @PostMapping("/authenticated/file/download")
    public ResponseEntity<Resource> downloadFile(@RequestBody FileRequest fileRequest) throws IOException {
        Resource resource = fileService.downloadFile(fileRequest.fileName(), fileRequest.entityName());
        Path filePath = fileService.getFilePath(fileRequest.fileName(), fileRequest.entityName());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(fileService.getFileHeaders(fileRequest.fileName())).body(resource);
    }

    @PostMapping("/authenticated/file/delete")
    public ResponseEntity<Boolean> deleteFile(@RequestBody FileRequest fileRequest) throws IOException {
        return ResponseEntity.ok(fileService.deleteFile(fileRequest.fileName(), fileRequest.entityName()));
    }
}
