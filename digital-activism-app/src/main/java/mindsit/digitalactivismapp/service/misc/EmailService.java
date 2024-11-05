package mindsit.digitalactivismapp.service.misc;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import mindsit.digitalactivismapp.modelDTO.authentication.EmailVerificationContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${custom.email.username:}")
    private String emailUsername;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(EmailVerificationContainer emailVerificationContainer) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

        mimeMessageHelper.setFrom(emailUsername);
        mimeMessageHelper.setTo(emailVerificationContainer.getTo());
        mimeMessageHelper.setSubject(emailVerificationContainer.getSubject());

        if (emailVerificationContainer.isHTML()) {
            Context context = new Context();
            context.setVariable("message", emailVerificationContainer.getBody());
            String processedString = templateEngine.process(emailVerificationContainer.getTemplate(), context);

            mimeMessageHelper.setText(processedString, true);
        } else {
            mimeMessageHelper.setText(emailVerificationContainer.getBody(), false);
        }

        mailSender.send(mimeMessage);
    }

    public void sendVerificationEmail(EmailVerificationContainer emailVerificationContainer) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

        mimeMessageHelper.setFrom(emailUsername);
        mimeMessageHelper.setTo(emailVerificationContainer.getTo());
        mimeMessageHelper.setSubject(emailVerificationContainer.getSubject());

        if (emailVerificationContainer.isHTML()) {
            Context context = new Context();
            context.setVariable("verificationCode", emailVerificationContainer.getVerificationCode());
            String processedString = templateEngine.process(emailVerificationContainer.getTemplate(), context);

            mimeMessageHelper.setText(processedString, true);
        } else {
            mimeMessageHelper.setText(emailVerificationContainer.getBody(), false);
        }

        mailSender.send(mimeMessage);
    }
}
