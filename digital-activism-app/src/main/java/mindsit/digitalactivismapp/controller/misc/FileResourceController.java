package mindsit.digitalactivismapp.controller.misc;

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

import static mindsit.digitalactivismapp.service.misc.FileService.DIRECTORY;

@RestController
@RequestMapping("/file/authenticated")
public class FileResourceController {

    protected final FileService fileService = new FileService();

    @PostMapping("/upload-files")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles) throws IOException {
        return ResponseEntity.ok(fileService.uploadFiles(multipartFiles, DIRECTORY));
    }

    @GetMapping("/download-file/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) throws IOException {
        Resource resource = fileService.downloadFile(fileName, "");
        Path filePath = fileService.getFilePath(fileName, "");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(fileService.getFileHeaders(fileName)).body(resource);
    }

    @GetMapping("/delete-file/{fileName}")
    public ResponseEntity<Boolean> deleteFile(@PathVariable("fileName") String fileName) throws IOException {
        return ResponseEntity.ok(fileService.deleteFile(fileName, ""));
    }
}
