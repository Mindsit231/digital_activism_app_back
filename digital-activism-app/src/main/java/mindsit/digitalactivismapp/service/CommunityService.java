package mindsit.digitalactivismapp.service;

import mindsit.digitalactivismapp.mapper.CommunityMapper;
import mindsit.digitalactivismapp.mapper.MemberMapper;
import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.model.community.MemberCommunity;
import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.model.post.Post;
import mindsit.digitalactivismapp.model.post.PostImage;
import mindsit.digitalactivismapp.model.post.PostVideo;
import mindsit.digitalactivismapp.model.tag.PostTag;
import mindsit.digitalactivismapp.model.tag.Tag;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.modelDTO.community.CommunityDTO;
import mindsit.digitalactivismapp.modelDTO.community.CommunityRequest;
import mindsit.digitalactivismapp.modelDTO.post.PostDTO;
import mindsit.digitalactivismapp.repository.MemberRepository;
import mindsit.digitalactivismapp.repository.community.CommunityRepository;
import mindsit.digitalactivismapp.repository.community.MemberCommunityRepository;
import mindsit.digitalactivismapp.service.member.MemberService;
import mindsit.digitalactivismapp.service.misc.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static mindsit.digitalactivismapp.custom.Functions.getToken;

@Service
public class CommunityService extends EntityService<Community, CommunityRepository> {
    protected final MemberRepository memberRepository;
    protected final MemberCommunityRepository memberCommunityRepository;
    protected final CommunityMapper communityMapper;
    protected final MemberMapper memberMapper;

    protected final FileService fileService = new FileService();
    private final MemberService memberService;

    public CommunityService(CommunityRepository repository,
                            MemberRepository memberRepository,
                            MemberCommunityRepository memberCommunityRepository,
                            CommunityMapper communityMapper, MemberMapper memberMapper, MemberService memberService) {
        super(repository);
        this.memberRepository = memberRepository;
        this.memberCommunityRepository = memberCommunityRepository;
        this.communityMapper = communityMapper;
        this.memberMapper = memberMapper;
        this.memberService = memberService;
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }

    public Integer getTableLength(String searchValue) {
        if(searchValue != null && !searchValue.isEmpty()) {
            return entityRepository.getTableLengthBySearchValue(searchValue.toLowerCase());
        } else {
            return entityRepository.getTableLength();
        }
    }

    public List<CommunityDTO> fetchCommunitiesLimited(FetchEntityLimited fetchEntityLimited, String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(memberRepository::findByToken);
        if (optionalMember.isPresent()) {
            if(fetchEntityLimited.searchValue() != null && !fetchEntityLimited.searchValue().isEmpty()) {
                return communityMapper.communityToCommunityDTO(entityRepository.fetchCommunitiesLimitedBySearchValue(fetchEntityLimited.limit(), fetchEntityLimited.offset(), fetchEntityLimited.searchValue().toLowerCase()), optionalMember.get());
            } else {
                return communityMapper.communityToCommunityDTO(entityRepository.fetchCommunitiesLimited(fetchEntityLimited.limit(), fetchEntityLimited.offset()), optionalMember.get());
            }
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public Boolean toggleJoin(Long communityId, String authHeader) {
        return getToken(authHeader).map(memberRepository::findByToken).map(member -> {
            if (memberCommunityRepository.findByCommunityIdAndMemberId(communityId, member.getId()) != null) {
                memberCommunityRepository.deleteByCommunityIdAndMemberId(communityId, member.getId());
            } else {
                MemberCommunity memberCommunity = new MemberCommunity(communityId, member.getId());
                memberCommunityRepository.save(memberCommunity);
            }
            return true;
        }).orElse(false);
    }

    public CommunityDTO findCommunityDTOById(Long communityId, String authHeader) {
        return getToken(authHeader).map(memberRepository::findByToken)
                .flatMap(member -> entityRepository.findById(communityId)
                        .map(community -> communityMapper.communityToCommunityDTO(community, member)))
                .orElse(null);

    }

    public List<CommunityDTO> fetchCommunitiesLimitedByMemberId(FetchEntityLimited fetchEntityLimited, String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(memberRepository::findByToken);
        if (optionalMember.isPresent()) {
            return communityMapper.communityToCommunityDTO(
                    entityRepository.fetchCommunitiesLimitedByMemberId(fetchEntityLimited.limit(), fetchEntityLimited.offset(), optionalMember.get().getId()),
                    optionalMember.get());
        } else {
            return new ArrayList<>();
        }
    }

    public Integer fetchCommunitiesCountByMemberId(String authHeader) {
        return getToken(authHeader).map(memberRepository::findByToken)
                .map(member -> entityRepository.fetchCommunitiesCountByMemberId(member.getId()))
                .orElse(0);
    }

    public ResponseEntity<CommunityDTO> addCommunity(CommunityRequest communityRequest, String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(memberRepository::findByToken);
        if (optionalMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            Member member = optionalMember.get();

            Community mappedCommunity = communityMapper.communityRequestToCommunity(communityRequest);
            Community outputCommunity = entityRepository.save(mappedCommunity);

            MemberCommunity memberCommunity = new MemberCommunity(outputCommunity.getId(), member.getId());
            memberCommunity.setIsAdmin(true);
            memberCommunityRepository.save(memberCommunity);

            Community fullOutputCommunity = entityRepository.findById(outputCommunity.getId()).orElse(null);

            if (fullOutputCommunity != null) {
                CommunityDTO communityDTO = communityMapper.communityToCommunityDTO(fullOutputCommunity, member);
                return ResponseEntity.ok(communityDTO);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

    }
}
