package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.modelDTO.CommunityDTO;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.service.CommunityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
