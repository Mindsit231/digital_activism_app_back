package mindsit.digitalactivismapp.service;

import mindsit.digitalactivismapp.mapper.CommunityMapper;
import mindsit.digitalactivismapp.mapper.MemberMapper;
import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.model.community.MemberCommunity;
import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.modelDTO.CommunityDTO;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.repository.MemberRepository;
import mindsit.digitalactivismapp.repository.community.CommunityRepository;
import mindsit.digitalactivismapp.repository.community.MemberCommunityRepository;
import mindsit.digitalactivismapp.service.member.MemberService;
import mindsit.digitalactivismapp.service.misc.FileService;
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
    private final CommunityRepository communityRepository;

    public CommunityService(CommunityRepository repository,
                            MemberRepository memberRepository,
                            MemberCommunityRepository memberCommunityRepository,
                            CommunityMapper communityMapper, MemberMapper memberMapper, MemberService memberService, CommunityRepository communityRepository) {
        super(repository);
        this.memberRepository = memberRepository;
        this.memberCommunityRepository = memberCommunityRepository;
        this.communityMapper = communityMapper;
        this.memberMapper = memberMapper;
        this.memberService = memberService;
        this.communityRepository = communityRepository;
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }

    public Integer getTableLength() {
        return entityRepository.getTableLength();
    }

    public List<CommunityDTO> fetchCommunitiesLimited(FetchEntityLimited fetchEntityLimited, String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(memberRepository::findByToken);
        if (optionalMember.isPresent()) {
            return communityMapper.communityToCommunityDTO(entityRepository.fetchCommunitiesLimited(fetchEntityLimited.limit(), fetchEntityLimited.offset()), optionalMember.get());
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

}
