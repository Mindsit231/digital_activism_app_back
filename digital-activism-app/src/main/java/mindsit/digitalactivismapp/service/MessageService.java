package mindsit.digitalactivismapp.service;

import mindsit.digitalactivismapp.model.message.Message;
import mindsit.digitalactivismapp.repository.message.MessageRepository;
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
