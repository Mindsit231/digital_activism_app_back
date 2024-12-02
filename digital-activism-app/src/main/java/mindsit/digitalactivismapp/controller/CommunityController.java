package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.modelDTO.CommunityDTO;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.service.CommunityService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static mindsit.digitalactivismapp.controller.AuthenticationController.AUTHORIZATION_HEADER;

@RestController
public class CommunityController extends EntityController<Community, CommunityService> {
    public CommunityController(CommunityService service) {
        super(service, Community.class);
    }

    @GetMapping("/authenticated/community/get-table-length")
    public ResponseEntity<Integer> getTableLength() {
        return ResponseEntity.ok(entityService.getTableLength());
    }

    @PostMapping("/authenticated/community/fetch-communities-limited")
    public ResponseEntity<List<CommunityDTO>> fetchCommunitiesLimited(@RequestBody FetchEntityLimited fetchEntityLimited) {
        return ResponseEntity.ok(entityService.fetchCommunitiesLimited(fetchEntityLimited));
    }

    @PostMapping("/authenticated/community/upload-files")
    public ResponseEntity<List<String>> uploadFiles(
            @RequestHeader(AUTHORIZATION_HEADER) String authHeader,
            @RequestParam("files") List<MultipartFile> multipartFiles)
            throws IOException {
        return super.uploadFiles(authHeader, multipartFiles);
    }

    @GetMapping("/authenticated/community/download-file/{fileName}")
    public ResponseEntity<Resource> downloadFile(
            @RequestHeader(AUTHORIZATION_HEADER) String authHeader,
            @PathVariable("fileName") String fileName) throws IOException {
        return super.downloadFile(authHeader, fileName);
    }

    @GetMapping("/authenticated/community/delete-file/{fileName}")
    public ResponseEntity<Boolean> deleteFile(
            @RequestHeader(AUTHORIZATION_HEADER) String authHeader,
            @PathVariable("fileName") String fileName) throws IOException {
        return super.deleteFile(authHeader, fileName);
    }

//    @GetMapping("/public/get-object")
//    public ResponseEntity<CommunityDTO> getObject() {
//        return ResponseEntity.ok(
//                new CommunityDTO(
//                        "Community Name",
//                        "Community Description",
//                        "logo name",
//                        fileService.encodeImageToBase64("test-logo.png", entityClass.getSimpleName().toLowerCase()),
//                        "Community Location",
//                        new Date()
//                )
//        );
//    }


}
