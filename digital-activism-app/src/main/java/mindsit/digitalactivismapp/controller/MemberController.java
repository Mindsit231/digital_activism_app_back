package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.model.query.update.PfpNameByEmail;
import mindsit.digitalactivismapp.model.tag.Tag;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.modelDTO.member.MemberDTO;
import mindsit.digitalactivismapp.modelDTO.member.UpdateRequest;
import mindsit.digitalactivismapp.modelDTO.member.UpdateResponse;
import mindsit.digitalactivismapp.service.member.MemberService;
import mindsit.digitalactivismapp.service.misc.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static mindsit.digitalactivismapp.controller.AuthenticationController.AUTHORIZATION_HEADER;

@RestController
public class MemberController extends EntityController<Member, MemberService> {

    private final FileService fileService;

    @Autowired
    public MemberController(MemberService memberService, FileService fileService) {
        super(memberService, Member.class);
        this.fileService = fileService;
    }

    // FOR TESTING PURPOSES
    @GetMapping("/public/member/find-by-id/{id}")
    public ResponseEntity<Member> findById(@PathVariable("id") Long id) {
        return super.findById(id);
    }

    @PostMapping("/authenticated/member/update-pfp-name-by-email")
    public ResponseEntity<Integer> updatePfpImgPathByEmail(
            @RequestBody PfpNameByEmail pfpNameByEmail) {
        return ResponseEntity.ok(entityService.updatePfpNameByEmail(pfpNameByEmail));
    }

    @PostMapping("/authenticated/member/update")
    public ResponseEntity<UpdateResponse> update(
            @RequestHeader(AUTHORIZATION_HEADER) String authHeader,
            @RequestBody UpdateRequest updateRequest) {
        return entityService.update(updateRequest, authHeader);
    }

    @PostMapping("/authenticated/member/propose-new-tag")
    public ResponseEntity<Tag> proposeNewTag(
            @RequestHeader(AUTHORIZATION_HEADER) String authHeader,
            @RequestBody String tagProposal) {
        return ResponseEntity.ok(entityService.proposeNewTag(tagProposal, authHeader));
    }

    @PostMapping("/authenticated/member/fetch-tags-by-token")
    public ResponseEntity<List<Tag>> fetchTagsByToken(
            @RequestHeader(AUTHORIZATION_HEADER) String authHeader) {
        return ResponseEntity.ok(entityService.fetchTagsByToken(authHeader));
    }

    @PostMapping("/authenticated/member/delete-tag-by-token")
    public ResponseEntity<Boolean> deleteTagByToken(
            @RequestHeader(AUTHORIZATION_HEADER) String authHeader,
            @RequestBody Tag tag) {
        return ResponseEntity.ok(entityService.deleteTagByToken(tag, authHeader));
    }

    @PostMapping("/authenticated/member/fetch-members-limited-by-community-id")
    public ResponseEntity<List<MemberDTO>> fetchMembersLimitedByCommunityId(@RequestHeader(AUTHORIZATION_HEADER) String authHeader, @RequestBody FetchEntityLimited fetchEntityLimited) {
        return entityService.fetchMembersLimitedByCommunityId(fetchEntityLimited, authHeader);
    }

    @GetMapping("/authenticated/member/fetch-members-count-by-community-id")
    public ResponseEntity<Integer> fetchMembersCountByCommunityId(@RequestHeader(AUTHORIZATION_HEADER) String authHeader, @RequestParam Long communityId) {
        return entityService.fetchMembersCountByCommunityId(communityId, authHeader);
    }

    @PostMapping("/authenticated/member/fetch-members-limited-by-campaign-id")
    public ResponseEntity<List<MemberDTO>> fetchMembersLimitedByCampaignId(@RequestHeader(AUTHORIZATION_HEADER) String authHeader, @RequestBody FetchEntityLimited fetchEntityLimited) {
        return entityService.fetchMembersLimitedByCampaignId(fetchEntityLimited, authHeader);
    }

    @GetMapping("/authenticated/member/fetch-members-count-by-campaign-id")
    public ResponseEntity<Integer> fetchMembersCountByCampaignId(@RequestHeader(AUTHORIZATION_HEADER) String authHeader, @RequestParam Long campaignId) {
        return entityService.fetchMembersCountByCampaignId(campaignId, authHeader);
    }

    @PostMapping("/authenticated/member/upload-files")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> multipartFiles) {
        return fileService.uploadFiles(multipartFiles, entityClass.getSimpleName());
    }

    @GetMapping("/public/member/download-file")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) {
        return fileService.downloadFile(fileName, entityClass.getSimpleName());
    }

    @GetMapping("/authenticated/member/delete-file")
    public ResponseEntity<Boolean> deleteFile(@RequestParam String fileName) {
        return fileService.deleteFile(fileName, entityClass.getSimpleName());
    }
}
