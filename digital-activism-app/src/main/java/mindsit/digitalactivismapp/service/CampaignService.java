package mindsit.digitalactivismapp.service;

import mindsit.digitalactivismapp.mapper.CampaignMapper;
import mindsit.digitalactivismapp.model.campaign.Campaign;
import mindsit.digitalactivismapp.model.campaign.MemberCampaign;
import mindsit.digitalactivismapp.model.community.MemberCommunity;
import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.modelDTO.campaign.CampaignDTO;
import mindsit.digitalactivismapp.modelDTO.campaign.CampaignRequest;
import mindsit.digitalactivismapp.repository.campaign.CampaignRepository;
import mindsit.digitalactivismapp.repository.MemberRepository;
import mindsit.digitalactivismapp.repository.campaign.MemberCampaignRepository;
import mindsit.digitalactivismapp.repository.community.MemberCommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static mindsit.digitalactivismapp.custom.Functions.getToken;

@Service
public class CampaignService extends EntityService<Campaign, CampaignRepository> {

    private final CampaignMapper campaignMapper;
    private final MemberRepository memberRepository;
    private final MemberCommunityRepository memberCommunityRepository;
    private final MemberCampaignRepository memberCampaignRepository;

    @Autowired
    public CampaignService(CampaignRepository repository, CampaignMapper campaignMapper, MemberRepository memberRepository, MemberCommunityRepository memberCommunityRepository, MemberCampaignRepository memberCampaignRepository) {
        super(repository);
        this.campaignMapper = campaignMapper;
        this.memberRepository = memberRepository;
        this.memberCommunityRepository = memberCommunityRepository;
        this.memberCampaignRepository = memberCampaignRepository;
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }

    public List<Campaign> fetchCampaignsLimitedByCommunityId(FetchEntityLimited fetchEntityLimited) {
        return entityRepository.fetchCampaignsLimitedByCommunityId(fetchEntityLimited.limit(), fetchEntityLimited.offset(), fetchEntityLimited.optionalId());
    }

    public List<CampaignDTO> fetchCampaignDTOSLimitedByCommunityId(FetchEntityLimited fetchEntityLimited, String authHeader) {
        return getToken(authHeader).map(memberRepository::findByToken)
                .map(member -> campaignMapper.campaignToCampaignDTO(fetchCampaignsLimitedByCommunityId(fetchEntityLimited), member))
                .orElseGet(ArrayList::new);
    }

    public Integer getTableLengthByCommunityId(Long communityId) {
        return entityRepository.getTableLengthByCommunityId(communityId);
    }

    public ResponseEntity<CampaignDTO> addCampaign(CampaignRequest campaignRequest, String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(memberRepository::findByToken);
        if (optionalMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            Member member = optionalMember.get();
            MemberCommunity memberCommunity = memberCommunityRepository.findByCommunityIdAndMemberId(campaignRequest.communityId(), member.getId());

            if (memberCommunity.getIsAdmin()) {
                Campaign mappedCampaign = campaignMapper.campaignRequestToCampaign(campaignRequest, member);
                Campaign outputCampaign = entityRepository.save(mappedCampaign);
                Campaign fullOutputCampaign = entityRepository.findById(outputCampaign.getId()).orElse(null);

                if (fullOutputCampaign != null) {
                    CampaignDTO campaignDTO = campaignMapper.campaignToCampaignDTO(fullOutputCampaign, member);
                    return ResponseEntity.ok(campaignDTO);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
    }

    @Transactional
    public Boolean toggleParticipate(Long campaignId, String authHeader) {
        return getToken(authHeader).map(memberRepository::findByToken).map(member -> {
            if (memberCampaignRepository.findByCampaignIdAndMemberId(campaignId, member.getId()) != null) {
                memberCampaignRepository.deleteByCampaignIdAndMemberId(campaignId, member.getId());
            } else {
                MemberCampaign memberCampaign = new MemberCampaign(campaignId, member.getId());
                memberCampaignRepository.save(memberCampaign);
            }
            return true;
        }).orElse(false);
    }

    public CampaignDTO findCampaignDTOById(Long campaignId, Long communityId, String authHeader) {
        return getToken(authHeader).map(memberRepository::findByToken)
                .flatMap(member -> entityRepository.findById(campaignId)
                        .map(campaign -> {
                            if (campaign.getCommunityId().equals(communityId)) {
                                return campaignMapper.campaignToCampaignDTO(campaign, member);
                            } else {
                                return null;
                            }
                        }))
                .orElse(null);
    }
}
