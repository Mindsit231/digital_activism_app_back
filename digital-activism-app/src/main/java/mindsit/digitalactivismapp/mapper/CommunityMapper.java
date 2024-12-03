package mindsit.digitalactivismapp.mapper;

import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.modelDTO.CommunityDTO;
import mindsit.digitalactivismapp.repository.community.MemberCommunityRepository;
import mindsit.digitalactivismapp.service.misc.FileService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CommunityMapper {
    protected final FileService fileService = new FileService();

    @Autowired
    protected MemberCommunityRepository memberCommunityRepository;

    @Mapping(target = "logo", expression = "java(fileService.getResource(community.getLogoName(), Community.class.getSimpleName().toLowerCase()))")
    @Mapping(target = "banner", expression = "java(fileService.getResource(community.getBannerName(), Community.class.getSimpleName().toLowerCase()))")
    @Mapping(target = "joined", expression = "java(memberCommunityRepository.findByCommunityIdAndMemberId(community.getId(), member.getId()) != null)")
    @Mapping(target = "id", source = "community.id")
    public abstract CommunityDTO communityToCommunityDTO(Community community, Member member);

    public List<CommunityDTO> communityToCommunityDTO(Collection<Community> communities, Member member) {
        List<CommunityDTO> communityDTOS = new ArrayList<>();
        for (Community community : communities) {
            communityDTOS.add(communityToCommunityDTO(community, member));
        }
        return communityDTOS;
    }
}
