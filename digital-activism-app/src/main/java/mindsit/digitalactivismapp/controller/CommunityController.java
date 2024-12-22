package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.modelDTO.CommunityDTO;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.service.CommunityService;
import mindsit.digitalactivismapp.service.misc.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static mindsit.digitalactivismapp.controller.AuthenticationController.AUTHORIZATION_HEADER;

@RestController
public class CommunityController extends EntityController<Community, CommunityService> {

    private final FileService fileService;

    @Autowired
    public CommunityController(CommunityService service, FileService fileService) {
        super(service, Community.class);
        this.fileService = fileService;
    }

    @GetMapping("/authenticated/community/get-table-length")
    public ResponseEntity<Integer> getTableLength() {
        return ResponseEntity.ok(entityService.getTableLength());
    }

    @PostMapping("/authenticated/community/fetch-communities-limited")
    public ResponseEntity<List<CommunityDTO>> fetchCommunitiesLimited(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                                                                      @RequestBody FetchEntityLimited fetchEntityLimited) {
        return ResponseEntity.ok(entityService.fetchCommunitiesLimited(fetchEntityLimited, authHeader));
    }

    @GetMapping("/authenticated/community/toggle-join")
    public ResponseEntity<Boolean> toggleJoin(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                                              @RequestParam Long communityId) {
        return ResponseEntity.ok(entityService.toggleJoin(communityId, authHeader));
    }

    @GetMapping("/authenticated/community/find-community-dto-by-id")
    public ResponseEntity<CommunityDTO> findById(@RequestHeader(AUTHORIZATION_HEADER) String authHeader, @RequestParam Long communityId) {
        return ResponseEntity.ok(entityService.findCommunityDTOById(communityId, authHeader));
    }

    @PostMapping("/authenticated/community/upload-files")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles) {
        return fileService.uploadFiles(multipartFiles, entityClass.getSimpleName());
    }

    @GetMapping("/authenticated/community/download-file")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) {
        return fileService.downloadFile(fileName, entityClass.getSimpleName());
    }

    @GetMapping("/authenticated/community/delete-file")
    public ResponseEntity<Boolean> deleteFile(@RequestParam String fileName) {
        return fileService.deleteFile(fileName, entityClass.getSimpleName());
    }
}
