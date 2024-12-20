package mindsit.digitalactivismapp.service.member;

import mindsit.digitalactivismapp.mapper.MemberMapper;
import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.model.query.update.PfpNameByEmail;
import mindsit.digitalactivismapp.model.tag.MemberTag;
import mindsit.digitalactivismapp.model.tag.Tag;
import mindsit.digitalactivismapp.modelDTO.authentication.errorList.ErrorList;
import mindsit.digitalactivismapp.modelDTO.member.UpdateRequest;
import mindsit.digitalactivismapp.modelDTO.member.UpdateResponse;
import mindsit.digitalactivismapp.repository.MemberRepository;
import mindsit.digitalactivismapp.repository.tag.MemberTagRepository;
import mindsit.digitalactivismapp.repository.tag.TagRepository;
import mindsit.digitalactivismapp.service.EntityService;
import mindsit.digitalactivismapp.service.authentication.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final MemberTagRepository memberTagRepository;
    private final MemberMapper memberMapper;
    private final AuthenticationService authenticationService;

    @Autowired
    public MemberService(MemberRepository memberRepository,
                         TagRepository tagRepository,
                         MemberTagRepository memberTagRepository,
                         MemberMapper memberMapper, AuthenticationService authenticationService) {
        super(memberRepository);
        this.tagRepository = tagRepository;
        this.memberTagRepository = memberTagRepository;
        this.memberMapper = memberMapper;
        this.authenticationService = authenticationService;
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
        String formattedTag = tagProposal.toLowerCase();
        Optional<Tag> existingTag = Optional.ofNullable(tagRepository.findByName(formattedTag));

        if (existingTag.isEmpty()) {
            tagRepository.save(new Tag(formattedTag));
        }

        Optional<Tag> optionalTag = Optional.ofNullable(tagRepository.findByName(formattedTag));

        if (optionalTag.isPresent()) {
            getToken(authHeader).map(entityRepository::findByToken).ifPresent(member -> {
                memberTagRepository.save(new MemberTag(member.getId(), optionalTag.get().getId()));

            });
            return optionalTag.get();
        } else {
            return null;
        }
    }

    public List<Tag> fetchTagsByToken(String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(entityRepository::findByToken);
        List<Tag> tags = new ArrayList<>();
        optionalMember.ifPresent(member -> member.getMemberTags().forEach(memberTag -> {
            Optional<Tag> optionalTag = tagRepository.findById(memberTag.getTagId());
            optionalTag.ifPresent(tags::add);
        }));
        return tags;
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
}
