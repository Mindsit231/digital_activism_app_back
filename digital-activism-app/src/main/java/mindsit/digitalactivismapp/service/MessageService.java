package mindsit.digitalactivismapp.service;

import mindsit.digitalactivismapp.mapper.MessageMapper;
import mindsit.digitalactivismapp.model.message.Message;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.modelDTO.message.MessageDTO;
import mindsit.digitalactivismapp.modelDTO.message.MessageRequest;
import mindsit.digitalactivismapp.repository.MemberRepository;
import mindsit.digitalactivismapp.repository.message.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static mindsit.digitalactivismapp.custom.Functions.getToken;

@Service
public class MessageService extends EntityService<Message, MessageRepository> {

    private final MessageMapper messageMapper;
    private final MemberRepository memberRepository;

    @Autowired
    public MessageService(MessageRepository entityRepository, MessageMapper messageMapper, MemberRepository memberRepository) {
        super(entityRepository);
        this.messageMapper = messageMapper;
        this.memberRepository = memberRepository;
    }

    @Transactional
    @Override
    public Integer deleteEntityById(Long id) {
        return entityRepository.deleteEntityById(id);
    }

    public ResponseEntity<MessageDTO> addMessage(MessageRequest messageRequest, String authHeader) {
        Message mappedMessage = messageMapper.messageRequestToMessage(messageRequest);
        Message outputMessage = entityRepository.save(mappedMessage);
        Message fullOutputMessage = entityRepository.findById(outputMessage.getId()).orElse(null);

        if(fullOutputMessage != null) {
            MessageDTO messageDTO = messageMapper.messageToMessageDTO(fullOutputMessage);
            return ResponseEntity.ok(messageDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public Integer getTableLengthByCampaignId(Long campaignId) {
        return entityRepository.getTableLengthByCampaignId(campaignId);
    }

    public List<Message> fetchLatestMessagesLimitedByCampaignId(FetchEntityLimited fetchEntityLimited) {
        return entityRepository.fetchLatestMessagesLimitedByCampaignId(fetchEntityLimited.limit(), fetchEntityLimited.offset(), fetchEntityLimited.optionalId());
    }

    public List<MessageDTO> fetchLatestMessageDTOSLimitedByCampaignId(FetchEntityLimited fetchEntityLimited) {
        return messageMapper.messageToMessageDTO(fetchLatestMessagesLimitedByCampaignId(fetchEntityLimited));
    }
}
