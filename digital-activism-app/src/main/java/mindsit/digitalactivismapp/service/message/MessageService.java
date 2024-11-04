package mindsit.digitalactivismapp.service.message;

import mindsit.digitalactivismapp.model.message.Message;
import mindsit.digitalactivismapp.repository.message.MessageRepository;
import mindsit.digitalactivismapp.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService extends EntityService<Message, MessageRepository> {
    @Autowired
    public MessageService(MessageRepository entityRepository) {
        super(entityRepository);
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }
}
