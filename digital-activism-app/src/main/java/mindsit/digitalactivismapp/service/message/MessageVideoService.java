package mindsit.digitalactivismapp.service.message;

import mindsit.digitalactivismapp.model.message.MessageVideo;
import mindsit.digitalactivismapp.repository.message.MessageVideoRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageVideoService extends EntityService<MessageVideo, MessageVideoRepository> {
    public MessageVideoService(MessageVideoRepository repository) {
        super(repository);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }
}
