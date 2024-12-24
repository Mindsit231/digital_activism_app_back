package mindsit.digitalactivismapp.mapper;

import mindsit.digitalactivismapp.model.campaign.Campaign;
import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.modelDTO.campaign.CampaignDTO;
import mindsit.digitalactivismapp.modelDTO.campaign.CampaignRequest;
import mindsit.digitalactivismapp.repository.campaign.CampaignRepository;
import mindsit.digitalactivismapp.repository.campaign.MemberCampaignRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CampaignMapper {
    @Autowired
    protected CampaignRepository campaignRepository;
    @Autowired
    protected MemberCampaignRepository memberCampaignRepository;
    @Autowired
    protected MemberMapper memberMapper;

    @Mapping(target = "id", source = "campaign.id")
    @Mapping(target = "creationDate", source = "campaign.creationDate")
    @Mapping(target = "memberDTO", expression = "java(memberMapper.memberToMemberDTOShort(member))")
    @Mapping(target = "participating", expression = "java(participating(campaign, member))")
    public abstract CampaignDTO campaignToCampaignDTO(Campaign campaign, Member member);

    public List<CampaignDTO> campaignToCampaignDTO(Collection<Campaign> campaigns, Member member) {
        return campaigns.stream().map(campaign -> campaignToCampaignDTO(campaign, member)).toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", expression = "java(new Date())")
    public abstract Campaign campaignRequestToCampaign(CampaignRequest campaignRequest, Member member);

    public Boolean participating(Campaign campaign, Member member) {
        return memberCampaignRepository.findByCampaignIdAndMemberId(campaign.getId(), member.getId()) != null;
    }
}
