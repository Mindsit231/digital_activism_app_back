package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.service.CommunityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/community")
public class CommunityController extends EntityController<Community, CommunityService> {
    public CommunityController(CommunityService service) {
        super(service, Community.class);
    }
}
