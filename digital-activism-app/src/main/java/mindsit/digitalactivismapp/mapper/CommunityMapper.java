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

    @Mapping(target = "id", source = "community.id")
    @Mapping(target = "joined", expression = "java(joined(community, member))")
    @Mapping(target = "isAdmin", expression = "java(isAdmin(community, member))")
    public abstract CommunityDTO communityToCommunityDTO(Community community, Member member);

    public List<CommunityDTO> communityToCommunityDTO(Collection<Community> communities, Member member) {
        return communities.stream().map(community -> communityToCommunityDTO(community, member)).toList();
    }

    public Boolean joined(Community community, Member member) {
        return memberCommunityRepository.findByCommunityIdAndMemberId(community.getId(), member.getId()) != null;
    }

    public Boolean isAdmin(Community community, Member member) {
        Boolean isAdmin = memberCommunityRepository.findIsAdminByCommunityIdAndMemberId(community.getId(), member.getId());
        return isAdmin != null && isAdmin;
    }
}
