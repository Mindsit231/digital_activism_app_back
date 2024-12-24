package mindsit.digitalactivismapp.controller.post;

import mindsit.digitalactivismapp.controller.EntityController;
import mindsit.digitalactivismapp.model.post.PostImage;
import mindsit.digitalactivismapp.service.misc.FileService;
import mindsit.digitalactivismapp.service.post.PostImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class PostImageController extends EntityController<PostImage, PostImageService> {

    private final FileService fileService;

    @Autowired
    public PostImageController(PostImageService postImageService, FileService fileService) {
        super(postImageService, PostImage.class);
        this.fileService = fileService;
    }

    @PostMapping("/authenticated/post-image/upload-files")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles) {
        return fileService.uploadFiles(multipartFiles, entityClass.getSimpleName());
    }

    @GetMapping("/public/post-image/download-file")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) {
        return fileService.downloadFile(fileName, entityClass.getSimpleName());
    }

    @GetMapping("/authenticated/post-image/delete-file")
    public ResponseEntity<Boolean> deleteFile(@RequestParam String fileName) {
        return fileService.deleteFile(fileName, entityClass.getSimpleName());
    }
}
