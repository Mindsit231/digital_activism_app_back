package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.model.MyEntity;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public abstract class EntityController<T extends MyEntity, S extends EntityService<T, ?>> {
    protected final S entityService;
    protected final Class<T> entityClass;

    protected EntityController(S entityService, Class<T> entityClass) {
        this.entityService = entityService;
        this.entityClass = entityClass;
    }

    public ResponseEntity<T> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(entityService.findById(id).orElse(null));
    }

}
