package mindsit.digitalactivismapp.controller.misc;

import jakarta.mail.MessagingException;
import mindsit.digitalactivismapp.model.misc.Email;
import mindsit.digitalactivismapp.service.misc.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/public/send-email")
    public boolean sendEmail(@RequestBody Email email) {
        try {
            emailService.sendEmail(email);
            return true;
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            return false;
        }

        // Disabled sending emails for now to avoid spamming
//        return true;
    }
}
