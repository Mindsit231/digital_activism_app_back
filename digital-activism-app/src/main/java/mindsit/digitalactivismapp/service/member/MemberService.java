package mindsit.digitalactivismapp.service.member;

import mindsit.digitalactivismapp.mapper.member.MemberDTOMapper;
import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.model.query.update.PfpNameByEmail;
import mindsit.digitalactivismapp.model.tag.MemberTag;
import mindsit.digitalactivismapp.model.tag.Tag;
import mindsit.digitalactivismapp.modelDTO.MemberDTO;
import mindsit.digitalactivismapp.modelDTO.authentication.errorList.ErrorList;
import mindsit.digitalactivismapp.modelDTO.authentication.errorList.ErrorLists;
import mindsit.digitalactivismapp.modelDTO.authentication.passwordReset.ResetPasswordRequest;
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

    private final TagRepository tagRepository;
    private final MemberTagRepository memberTagRepository;
    private final MemberDTOMapper memberDTOMapper;
    private final AuthenticationService authenticationService;
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    public MemberService(MemberRepository memberRepository,
                         TagRepository tagRepository,
                         MemberTagRepository memberTagRepository, MemberDTOMapper memberDTOMapper, AuthenticationService authenticationService) {
        super(memberRepository);
        this.tagRepository = tagRepository;
        this.memberTagRepository = memberTagRepository;
        this.memberDTOMapper = memberDTOMapper;
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
    public ResponseEntity<MemberDTO> update(Member member, String authHeader) {
        Optional<Member> optionalMember = getToken(authHeader).map(entityRepository::findByToken);
        ErrorLists errorLists = new ErrorLists();

        if (optionalMember.isPresent()) {
            authenticationService.checkUsername(errorLists, member);
            authenticationService.checkEmail(errorLists, member);

            if(member.getUsername() != null && !member.getUsername().isEmpty() && !errorLists.findErrorListByName(USERNAME_ERROR_LIST).hasErrors()) {
                logger.info("Username updated from '{}' to '{}' for memberId '{}'", optionalMember.get().getUsername(), member.getUsername(), optionalMember.get().getId());
                optionalMember.get().setUsername(member.getUsername());
            }
            if(member.getEmail() != null && !member.getEmail().isEmpty() && !errorLists.findErrorListByName(EMAIL_ERROR_LIST).hasErrors()) {
                logger.info("Email updated from '{}' to '{}' for memberId '{}'", optionalMember.get().getEmail(), member.getEmail(), optionalMember.get().getId());
                optionalMember.get().setEmail(member.getEmail());
            }

            if (member.getPassword() != null && !member.getPassword().isEmpty()) {
                authenticationService.resetPasswordSub(errorLists, member.getPassword(), authHeader);
            }

            if(errorLists.hasNoErrors()) {
                updateEntity(optionalMember.get());
                return ResponseEntity.ok(memberDTOMapper.apply(optionalMember.get()));
            } else {
                return ResponseEntity.badRequest().body(memberDTOMapper.apply(optionalMember.get()));
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
