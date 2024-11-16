package mindsit.digitalactivismapp.service.member;

import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.model.query.update.PfpNameByEmail;
import mindsit.digitalactivismapp.model.tag.MemberTag;
import mindsit.digitalactivismapp.model.tag.Tag;
import mindsit.digitalactivismapp.repository.MemberRepository;
import mindsit.digitalactivismapp.repository.tag.MemberTagRepository;
import mindsit.digitalactivismapp.repository.tag.TagRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static mindsit.digitalactivismapp.custom.Functions.getToken;

@Service
public class MemberService extends EntityService<Member, MemberRepository> {

    private final TagRepository tagRepository;
    private final MemberTagRepository memberTagRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository,
                         TagRepository tagRepository,
                         MemberTagRepository memberTagRepository) {
        super(memberRepository);
        this.tagRepository = tagRepository;
        this.memberTagRepository = memberTagRepository;
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

        if(optionalTag.isPresent()) {
            getToken(authHeader).map(entityRepository::findByToken).ifPresent(member -> {
                memberTagRepository.save(new MemberTag(member.getId(), optionalTag.get().getId()));

            });
            return optionalTag.get();
        } else {
            return null;
        }
    }

//    public List<Tag> fetchTagsByToken(String authHeader) {
//        Optional<Member> member =  getToken(authHeader).map(entityRepository::findByToken);
//
//
//
//    }
}
