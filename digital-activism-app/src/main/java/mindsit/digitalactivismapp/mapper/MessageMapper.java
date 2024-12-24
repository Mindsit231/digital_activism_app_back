package mindsit.digitalactivismapp.mapper;

import mindsit.digitalactivismapp.model.member.Member;
import mindsit.digitalactivismapp.model.message.Message;
import mindsit.digitalactivismapp.modelDTO.message.MessageDTO;
import mindsit.digitalactivismapp.modelDTO.message.MessageRequest;
import mindsit.digitalactivismapp.repository.message.MessageRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MessageMapper {
    @Autowired
    protected MessageRepository messageRepository;
    @Autowired
    protected MemberMapper memberMapper;

    @Mapping(target = "id", source = "message.id")
    @Mapping(target = "timestamp", source = "message.timestamp")
    @Mapping(target = "memberDTO", expression = "java(memberMapper.memberToMemberDTOShort(message.getMember()))")
    public abstract MessageDTO messageToMessageDTO(Message message);

    public List<MessageDTO> messageToMessageDTO(List<Message> messages) {
        return messages.stream().map(this::messageToMessageDTO).toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", expression = "java(new Date())")
    public abstract Message messageRequestToMessage(MessageRequest messageRequest);
}
