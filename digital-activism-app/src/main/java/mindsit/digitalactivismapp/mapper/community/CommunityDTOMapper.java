package mindsit.digitalactivismapp.mapper.community;

import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.model.community.MemberCommunity;
import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.modelDTO.CommunityDTO;
import mindsit.digitalactivismapp.repository.community.MemberCommunityRepository;
import mindsit.digitalactivismapp.service.misc.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class CommunityDTOMapper implements Function<Community, CommunityDTO> {
    protected final FileService fileService = new FileService();
    protected final MemberCommunityRepository memberCommunityRepository;

    @Autowired
    public CommunityDTOMapper(MemberCommunityRepository memberCommunityRepository) {
        this.memberCommunityRepository = memberCommunityRepository;
    }

    @Override
    public CommunityDTO apply(Community community) {
        Optional<MemberCommunity> memberCommunity = memberCommunityRepository.findById(community.getId());

        return new CommunityDTO(
                community.getName(),
                community.getDescription(),
                community.getLogoName(),
                fileService.getResource(community.getLogoName(), Community.class.getSimpleName().toLowerCase()),
                community.getBannerName(),
                fileService.getResource(community.getBannerName(), Community.class.getSimpleName().toLowerCase()),
                community.getTimestamp(),
                memberCommunity.isPresent()
        );
    }
}
