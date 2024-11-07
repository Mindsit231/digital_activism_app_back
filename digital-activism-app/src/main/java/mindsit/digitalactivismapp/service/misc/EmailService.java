package mindsit.digitalactivismapp.service.misc;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import mindsit.digitalactivismapp.model.misc.EmailContainer;
import mindsit.digitalactivismapp.modelDTO.authentication.verifyEmail.EmailVerificationContainer;
import mindsit.digitalactivismapp.modelDTO.authentication.passwordRecovery.PasswordRecoveryContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Value("${spring.mail.username:}")
    private String emailUsername;

    @Value("${custom.url.frontend:}")
    private String frontendUrl;

    @Value("${spring.mail.enabled:false}")
    private boolean mailEnabled;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    private MimeMessageHelper getMimeMessageHelper(EmailContainer emailContainer) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

        mimeMessageHelper.setFrom(emailUsername);
        mimeMessageHelper.setTo(emailContainer.getTo());
        mimeMessageHelper.setSubject(emailContainer.getSubject());

        logger.info("Mailing Service enabled: {}", mailEnabled);

        return mimeMessageHelper;
    }

    public void sendVerificationEmail(EmailVerificationContainer emailVerificationContainer) throws MessagingException {
        MimeMessageHelper mimeMessageHelper = getMimeMessageHelper(emailVerificationContainer);

        if (emailVerificationContainer.isHTML()) {
            Context context = new Context();
            context.setVariable("verificationCode", emailVerificationContainer.getVerificationCode());
            String processedString = templateEngine.process(emailVerificationContainer.getTemplate(), context);

            mimeMessageHelper.setText(processedString, true);
        } else {
            mimeMessageHelper.setText(emailVerificationContainer.getBody(), false);
        }

        if(mailEnabled) mailSender.send(mimeMessageHelper.getMimeMessage());
    }

    public void sendPasswordRecoveryEmail(PasswordRecoveryContainer passwordRecoveryContainer) throws MessagingException {
        MimeMessageHelper mimeMessageHelper = getMimeMessageHelper(passwordRecoveryContainer);

        if (passwordRecoveryContainer.isHTML()) {
            Context context = new Context();
            String url = frontendUrl + "/" + passwordRecoveryContainer.getPagePath() + "/" + passwordRecoveryContainer.getToken();
            context.setVariable("message", url);
            String processedString = templateEngine.process(passwordRecoveryContainer.getTemplate(), context);

            logger.info("Sending password recovery email to: {}, with recovery link: {}", passwordRecoveryContainer.getTo(), url);
            mimeMessageHelper.setText(processedString, true);
        } else {
            mimeMessageHelper.setText(passwordRecoveryContainer.getBody(), false);
        }

        if(mailEnabled) mailSender.send(mimeMessageHelper.getMimeMessage());
    }
}
