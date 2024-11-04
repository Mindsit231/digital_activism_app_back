package mindsit.digitalactivismapp.service.message;

import mindsit.digitalactivismapp.model.message.MessageImage;
import mindsit.digitalactivismapp.repository.message.MessageImageRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageImageService extends EntityService<MessageImage, MessageImageRepository> {
    public MessageImageService(MessageImageRepository repository) {
        super(repository);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }
}
