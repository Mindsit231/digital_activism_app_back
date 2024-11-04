package mindsit.digitalactivismapp.service;

import jakarta.persistence.EntityNotFoundException;
import mindsit.digitalactivismapp.model.MyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public abstract class EntityService<T extends MyEntity, R extends JpaRepository<T, Long>> {
    protected final R entityRepository;

    public EntityService(R entityRepository) {
        this.entityRepository = entityRepository;
    }

    public T addEntity(T entity) {
        return entityRepository.save(entity);
    }

    public List<T> findAllEntities() {
        return entityRepository.findAll();
    }

    public T findEntityById(Long id) {
        return entityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("MyEntity not found with id: " + id));
    }

    public T updateEntity(T entity) {
        return entityRepository.save(entity);
    }

    public abstract Integer deleteEntityById(Long id);

    public List<T> findAllLimitedEntities(List<Long> excludeIds, int limit) {
        List<T> entities = findAllEntities();

        entities = entities.stream()
                .filter(entity -> !excludeIds.contains(entity.getId()))
                .limit(limit)
                .collect(Collectors.toList());

        return entities;
    }
}
