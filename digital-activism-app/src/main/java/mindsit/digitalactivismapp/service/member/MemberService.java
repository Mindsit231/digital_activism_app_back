package mindsit.digitalactivismapp.service.member;

import mindsit.digitalactivismapp.mapper.MemberMapper;
import mindsit.digitalactivismapp.model.campaign.Campaign;
import mindsit.digitalactivismapp.model.community.MemberCommunity;
import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.model.query.update.PfpNameByEmail;
import mindsit.digitalactivismapp.model.tag.MemberTag;
import mindsit.digitalactivismapp.model.tag.Tag;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.modelDTO.authentication.errorList.ErrorList;
import mindsit.digitalactivismapp.modelDTO.member.MemberDTO;
import mindsit.digitalactivismapp.modelDTO.member.UpdateRequest;
import mindsit.digitalactivismapp.modelDTO.member.UpdateResponse;
import mindsit.digitalactivismapp.repository.MemberRepository;
import mindsit.digitalactivismapp.repository.campaign.CampaignRepository;
import mindsit.digitalactivismapp.repository.campaign.MemberCampaignRepository;
import mindsit.digitalactivismapp.repository.community.CommunityRepository;
import mindsit.digitalactivismapp.repository.community.MemberCommunityRepository;
import mindsit.digitalactivismapp.repository.tag.MemberTagRepository;
import mindsit.digitalactivismapp.repository.tag.TagRepository;
import mindsit.digitalactivismapp.service.EntityService;
import mindsit.digitalactivismapp.service.TagService;
import mindsit.digitalactivismapp.service.authentication.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static mindsit.digitalactivismapp.custom.Functions.getToken;
import static mindsit.digitalactivismapp.service.authentication.AuthenticationService.EMAIL_ERROR_LIST;
import static mindsit.digitalactivismapp.service.authentication.AuthenticationService.USERNAME_ERROR_LIST;

@Service
public class MemberService extends EntityService<Member, MemberRepository> {

    public final static String MEMBER_ERROR_LIST = "Member";
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    private final TagRepository tagRepository;
    private final TagService tagService;
    private final MemberTagRepository memberTagRepository;
    private final MemberMapper memberMapper;
    private final AuthenticationService authenticationService;

    private final MemberCommunityRepository memberCommunityRepository;
    private final CommunityRepository communityRepository;
    private final MemberCampaignRepository memberCampaignRepository;
    private final CampaignRepository campaignRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository,
                         TagRepository tagRepository, TagService tagService,
                         MemberTagRepository memberTagRepository,
                         MemberMapper memberMapper, AuthenticationService authenticationService, MemberCommunityRepository memberCommunityRepository, CommunityRepository communityRepository, MemberCampaignRepository memberCampaignRepository, CampaignRepository campaignRepository) {
        super(memberRepository);
        this.tagRepository = tagRepository;
        this.tagService = tagService;
        this.memberTagRepository = memberTagRepository;
        this.memberMapper = memberMapper;
        this.authenticationService = authenticationService;
        this.memberCommunityRepository = memberCommunityRepository;
        this.communityRepository = communityRepository;
        this.memberCampaignRepository = memberCampaignRepository;
        this.campaignRepository = campaignRepository;
    }

    public Optional<Member> findById(Long id) {
        return entityRepository.findById(id);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }

    @Transactional
    public Integer updatePfpNameByEmail(PfpNameByEmail pfpNameByEmail) {
        return entityRepository.updatePfpNameByEmail(pfpNameByEmail.getEmail(), pfpNameByEmail.getPfpName());
    }

    public Tag proposeNewTag(String tagProposal, String authHeader) {
        Optional<Tag> optionalTag = tagService.save(tagProposal);

        if (optionalTag.isPresent()) {
            getToken(authHeader).map(entityRepository::findByToken).ifPresent(member ->
                    memberTagRepository.save(new MemberTag(member.getId(), optionalTag.get().getId())));
            return optionalTag.get();
        } else {
            return null;
        }
    }

    public List<Tag> fetchTagsByToken(String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(entityRepository::findByToken);
        List<Tag> tagList = new ArrayList<>();
        optionalMember.ifPresent(member -> member.getMemberTags().forEach(memberTag -> {
            Optional<Tag> optionalTag = tagRepository.findById(memberTag.getTagId());
            optionalTag.ifPresent(tagList::add);
        }));
        return tagList;
    }

    @Transactional
    public Boolean deleteTagByToken(Tag tag, String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(entityRepository::findByToken);
        Optional<Tag> optionalTag = tagRepository.findById(tag.getId());

        if (optionalMember.isPresent() && optionalTag.isPresent()) {
            memberTagRepository.deleteByMemberIdAndTagId(optionalMember.get().getId(), optionalTag.get().getId());
            return true;
        } else {
            return false;
        }

    }

    @Transactional
    public ResponseEntity<UpdateResponse> update(UpdateRequest updateRequest, String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(entityRepository::findByToken);
        UpdateResponse updateResponse = new UpdateResponse();

        if (optionalMember.isPresent()) {
            authenticationService.checkUsername(updateResponse.getErrorLists(), updateRequest);
            authenticationService.checkEmail(updateResponse.getErrorLists(), updateRequest);
            authenticationService.resetPasswordSub(updateResponse.getErrorLists(), updateRequest.getPassword(), authHeader);

            if (!updateResponse.getErrorLists().findErrorListByName(USERNAME_ERROR_LIST).hasErrors()) {
                logger.info("Username updated from '{}' to '{}' for memberId '{}'", optionalMember.get().getUsername(), updateRequest.getUsername(), optionalMember.get().getId());
                optionalMember.get().setUsername(updateRequest.getUsername());
            }
            if (!updateResponse.getErrorLists().findErrorListByName(EMAIL_ERROR_LIST).hasErrors()) {
                logger.info("Email updated from '{}' to '{}' for memberId '{}'", optionalMember.get().getEmail(), updateRequest.getEmail(), optionalMember.get().getId());
                optionalMember.get().setEmail(updateRequest.getEmail());
            }

            if (updateResponse.getErrorLists().hasNoErrors()) {
                updateEntity(optionalMember.get());
                updateResponse.setMemberDTO(memberMapper.memberToMemberDTO(optionalMember.get()));
            }

        } else {
            ErrorList errorList = new ErrorList(MEMBER_ERROR_LIST);
            errorList.getErrors().add("Member not found");
            updateResponse.getErrorLists().getErrorList().add(errorList);
        }

        return ResponseEntity.ok(updateResponse);
    }

    public ResponseEntity<List<MemberDTO>> fetchMembersLimitedByCommunityId(FetchEntityLimited fetchEntityLimited, String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(entityRepository::findByToken);
        if(optionalMember.isPresent()) {
            MemberCommunity memberCommunity = memberCommunityRepository.findByCommunityIdAndMemberId(fetchEntityLimited.optionalId(), optionalMember.get().getId());

            if (memberCommunity != null && memberCommunity.getIsAdmin()) {
                List<Member> members = entityRepository.fetchMembersLimitedByCommunityId(fetchEntityLimited.limit(), fetchEntityLimited.offset(), fetchEntityLimited.optionalId());
                return ResponseEntity.ok(memberMapper.memberToMemberDTOShort(members));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<Integer> fetchMembersCountByCommunityId(Long communityId, String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(entityRepository::findByToken);
        if(optionalMember.isPresent()) {
            MemberCommunity memberCommunity = memberCommunityRepository.findByCommunityIdAndMemberId(communityId, optionalMember.get().getId());

            if (memberCommunity != null && memberCommunity.getIsAdmin()) {
                Integer membersCount = entityRepository.fetchMembersCountByCommunityId(communityId);
                return ResponseEntity.ok(membersCount);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<List<MemberDTO>> fetchMembersLimitedByCampaignId(FetchEntityLimited fetchEntityLimited, String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(entityRepository::findByToken);
        if(optionalMember.isPresent()) {
            Campaign campaign = campaignRepository.findById(fetchEntityLimited.optionalId()).orElse(null);
            if(campaign == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            MemberCommunity memberCommunity = memberCommunityRepository.findByCommunityIdAndMemberId(campaign.getCommunityId(), optionalMember.get().getId());

            if (memberCommunity != null && memberCommunity.getIsAdmin()) {
                List<Member> members = entityRepository.fetchMembersLimitedByCampaignId(fetchEntityLimited.limit(), fetchEntityLimited.offset(), fetchEntityLimited.optionalId());
                return ResponseEntity.ok(memberMapper.memberToMemberDTOShort(members));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Integer> fetchMembersCountByCampaignId(Long campaignId, String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(entityRepository::findByToken);
        if(optionalMember.isPresent()) {
            Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
            if(campaign == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            MemberCommunity memberCommunity = memberCommunityRepository.findByCommunityIdAndMemberId(campaign.getCommunityId(), optionalMember.get().getId());

            if (memberCommunity != null && memberCommunity.getIsAdmin()) {
                Integer membersCount = entityRepository.fetchMembersCountByCampaignId(campaignId);
                return ResponseEntity.ok(membersCount);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
