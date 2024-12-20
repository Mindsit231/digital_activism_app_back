package mindsit.digitalactivismapp.service;

import mindsit.digitalactivismapp.model.MyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public abstract class EntityService<T extends MyEntity, R extends JpaRepository<T, Long>> {
    protected final R entityRepository;

    public EntityService(R entityRepository) {
        this.entityRepository = entityRepository;
    }

    public T updateEntity(T entity) {
        return entityRepository.save(entity);
    }

    public abstract Integer deleteEntityById(Long id);

    public Optional<T> findById(Long id) {
        return entityRepository.findById(id);
    }
}
