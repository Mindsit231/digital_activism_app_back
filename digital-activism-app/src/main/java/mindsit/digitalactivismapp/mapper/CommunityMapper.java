package mindsit.digitalactivismapp.mapper;

import mindsit.digitalactivismapp.model.community.Community;
import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.modelDTO.CommunityDTO;
import mindsit.digitalactivismapp.repository.community.MemberCommunityRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class CommunityMapper {
    @Autowired
    protected MemberCommunityRepository memberCommunityRepository;

    @Mapping(target = "joined", expression = "java(memberCommunityRepository.findByCommunityIdAndMemberId(community.getId(), member.getId()) != null)")
    @Mapping(target = "isAdmin", expression = "java(isAdmin(community, member))")
    @Mapping(target = "id", source = "community.id")
    public abstract CommunityDTO communityToCommunityDTO(Community community, Member member);

    public List<CommunityDTO> communityToCommunityDTO(Collection<Community> communities, Member member) {
        List<CommunityDTO> communityDTOS = new ArrayList<>();
        for (Community community : communities) {
            communityDTOS.add(communityToCommunityDTO(community, member));
        }
        return communityDTOS;
    }

    public Boolean isAdmin(Community community, Member member) {
        Boolean isAdmin = memberCommunityRepository.findIsAdminByCommunityIdAndMemberId(community.getId(), member.getId());

        return isAdmin != null && isAdmin;
    }
}
