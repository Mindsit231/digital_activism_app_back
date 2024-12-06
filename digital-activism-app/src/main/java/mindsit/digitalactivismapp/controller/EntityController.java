package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.model.MyEntity;
import mindsit.digitalactivismapp.service.EntityService;
import mindsit.digitalactivismapp.service.misc.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static mindsit.digitalactivismapp.controller.AuthenticationController.AUTHORIZATION_HEADER;

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
