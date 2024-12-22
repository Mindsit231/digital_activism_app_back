package mindsit.digitalactivismapp.service;

import mindsit.digitalactivismapp.model.tag.MemberTag;
import mindsit.digitalactivismapp.model.tag.Tag;
import mindsit.digitalactivismapp.repository.tag.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static mindsit.digitalactivismapp.custom.Functions.getToken;

@Service
public class TagService extends EntityService<Tag, TagRepository> {
    public TagService(TagRepository repository) {
        super(repository);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }

    public Optional<Tag> save(String tagProposal) {
        String formattedTag = tagProposal.toLowerCase();
        Optional<Tag> existingTag = Optional.ofNullable(entityRepository.findByName(formattedTag));

        if (existingTag.isEmpty()) {
            entityRepository.save(new Tag(formattedTag));
        }

        return Optional.ofNullable(entityRepository.findByName(formattedTag));
    }

    public List<Tag> saveAll(List<String> tagProposals) {
        List<Tag> tagList = new ArrayList<>();

        tagProposals.forEach(tagProposal -> {
            Optional<Tag> existingTag = save(tagProposal);
            existingTag.ifPresent(tagList::add);
        });

        return tagList;
    }
}
