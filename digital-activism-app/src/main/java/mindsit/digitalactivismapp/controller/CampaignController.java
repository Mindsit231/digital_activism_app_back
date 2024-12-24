package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.model.campaign.Campaign;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.modelDTO.campaign.CampaignDTO;
import mindsit.digitalactivismapp.modelDTO.campaign.CampaignRequest;
import mindsit.digitalactivismapp.service.CampaignService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static mindsit.digitalactivismapp.controller.AuthenticationController.AUTHORIZATION_HEADER;

@RestController
public class CampaignController extends EntityController<Campaign, CampaignService> {
    public CampaignController(CampaignService service) {
        super(service, Campaign.class);
    }

    @PostMapping("/authenticated/community-admin/campaign/add")
    public ResponseEntity<CampaignDTO> addPost(@RequestBody CampaignRequest campaignRequest, @RequestHeader(AUTHORIZATION_HEADER) String authHeader) {
        return entityService.addCampaign(campaignRequest, authHeader);
    }

    @GetMapping("/authenticated/campaign/get-table-length-by-community-id")
    public ResponseEntity<Integer> getTableLengthByCommunityId(@RequestParam Long communityId) {
        return ResponseEntity.ok(entityService.getTableLengthByCommunityId(communityId));
    }

    @PostMapping("/authenticated/campaign/fetch-limited-by-community-id")
    public ResponseEntity<List<CampaignDTO>> fetchPostDTOSLimitedByCommunityId(@RequestBody FetchEntityLimited fetchEntityLimited,
                                                                               @RequestHeader(AUTHORIZATION_HEADER) String authHeader) {
        return ResponseEntity.ok(entityService.fetchCampaignDTOSLimitedByCommunityId(fetchEntityLimited, authHeader));
    }

    @PostMapping("/authenticated/campaign/fetch-campaigns-limited-by-member-id")
    public ResponseEntity<List<CampaignDTO>> fetchCampaignsLimitedByMemberId(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                                                                               @RequestBody FetchEntityLimited fetchEntityLimited) {
        return ResponseEntity.ok(entityService.fetchCampaignsLimitedByMemberId(fetchEntityLimited, authHeader));
    }

    @GetMapping("/authenticated/campaign/fetch-campaigns-count-by-member-id")
    public ResponseEntity<Integer> fetchCampaignsCountByMemberId(@RequestHeader(AUTHORIZATION_HEADER) String authHeader) {
        return ResponseEntity.ok(entityService.fetchCampaignsCountByMemberId(authHeader));
    }

    @GetMapping("/authenticated/campaign/toggle-participate")
    public ResponseEntity<Boolean> toggleJoin(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                                              @RequestParam Long campaignId) {
        return ResponseEntity.ok(entityService.toggleParticipate(campaignId, authHeader));
    }

    @GetMapping("/authenticated/campaign/find-campaign-dto-by-id")
    public ResponseEntity<CampaignDTO> findById(@RequestHeader(AUTHORIZATION_HEADER) String authHeader,
                                                @RequestParam Long campaignId,
                                                @RequestParam Long communityId) {
        return ResponseEntity.ok(entityService.findCampaignDTOById(campaignId, communityId, authHeader));
    }
}
