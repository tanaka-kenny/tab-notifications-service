package za.co.pacifish.notification_service.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import za.co.pacifish.notification_service.dto.EmailParts;
import za.co.pacifish.notification_service.dto.NotificationRequest;
import za.co.pacifish.notification_service.exception.SendNotificationException;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailHelper {

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;


    protected void sendEmail(NotificationRequest request)
        throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        EmailParts emailParts = getEmailParts(request);
        helper.setTo(request.recipient());
        helper.setSubject(emailParts.subject());
        helper.setText(emailParts.body(), true);

        mailSender.send(message);
    }

    private EmailParts getEmailParts(NotificationRequest request) {
        try {
            Template template = freemarkerConfig.getTemplate(request.template());
            String htmlOutput = FreeMarkerTemplateUtils.processTemplateIntoString(template, request.variables());
            String[] templateParts = htmlOutput.split("-----");
            if (templateParts.length != 2) {
                log.error("Parsing error for template {}", request.template());
                throw new SendNotificationException("An error occurred");
            }
            String subject = templateParts[0].trim();
            String body = templateParts[1].trim();

            return new EmailParts(subject, body);
        } catch (IOException | TemplateException e) {
            log.error("Error while generating email parts for template {} and recipient {}", request.template(), request.recipient(), e);
            throw new SendNotificationException("Error while sending email to " + request.recipient());
        }
    }

}
