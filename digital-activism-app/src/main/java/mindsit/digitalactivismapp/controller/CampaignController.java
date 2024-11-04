package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.model.Campaign;
import mindsit.digitalactivismapp.service.CampaignService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/campaign")
public class CampaignController extends EntityController<Campaign, CampaignService> {
    public CampaignController(CampaignService service) {
        super(service, Campaign.class);
    }
}
