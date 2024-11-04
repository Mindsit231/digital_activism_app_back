package mindsit.digitalactivismapp.service;

import mindsit.digitalactivismapp.model.Campaign;
import mindsit.digitalactivismapp.repository.CampaignRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CampaignService extends EntityService<Campaign, CampaignRepository> {
    public CampaignService(CampaignRepository repository) {
        super(repository);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }
}
