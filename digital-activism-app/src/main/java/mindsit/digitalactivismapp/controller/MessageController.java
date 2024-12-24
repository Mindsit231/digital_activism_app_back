package mindsit.digitalactivismapp.controller;

import mindsit.digitalactivismapp.model.message.Message;
import mindsit.digitalactivismapp.modelDTO.FetchEntityLimited;
import mindsit.digitalactivismapp.modelDTO.message.MessageDTO;
import mindsit.digitalactivismapp.modelDTO.message.MessageRequest;
import mindsit.digitalactivismapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static mindsit.digitalactivismapp.controller.AuthenticationController.AUTHORIZATION_HEADER;

@RestController
public class MessageController extends EntityController<Message, MessageService> {

    @Autowired
    public MessageController(MessageService service) {
        super(service, Message.class);
    }

    @PostMapping("/authenticated/message/add")
    public ResponseEntity<MessageDTO> addMessage(@RequestBody MessageRequest messageRequest, @RequestHeader(AUTHORIZATION_HEADER) String authHeader) {
        return entityService.addMessage(messageRequest, authHeader);
    }

    @GetMapping("/authenticated/message/get-table-length-by-campaign-id")
    public ResponseEntity<Integer> getTableLengthByCampaignId(@RequestParam Long campaignId) {
        return ResponseEntity.ok(entityService.getTableLengthByCampaignId(campaignId));
    }

    @PostMapping("/authenticated/message/fetch-latest-limited-by-campaign-id")
    public ResponseEntity<List<MessageDTO>> fetchLatestMessageDTOSLimitedByCampaignId(@RequestBody FetchEntityLimited fetchEntityLimited) {
        return ResponseEntity.ok(entityService.fetchLatestMessageDTOSLimitedByCampaignId(fetchEntityLimited));
    }
}
