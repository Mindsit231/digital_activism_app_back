package mindsit.digitalactivismapp.service;

import mindsit.digitalactivismapp.model.tag.Tag;
import mindsit.digitalactivismapp.repository.tag.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public Optional<Tag> findTagByName(String name) {
        return Optional.ofNullable(entityRepository.findByName(name));
    }

    public void save(Tag tag) {
        entityRepository.save(tag);
    }
}
