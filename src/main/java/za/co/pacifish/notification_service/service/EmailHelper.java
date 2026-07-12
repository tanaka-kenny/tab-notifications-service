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
import za.co.pacifish.notification_service.dto.SendEmailRequest;
import za.co.pacifish.notification_service.exception.SendEmailException;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailHelper {

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;


    protected void sendEmail(SendEmailRequest.EmailRequest request, Map<String, Object> model)
        throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

        EmailParts emailParts = getEmailParts(request, model);
        helper.setTo(request.to());
        helper.setSubject(emailParts.subject());
        helper.setText(emailParts.body(), true);

        mailSender.send(message);
    }

    private EmailParts getEmailParts(SendEmailRequest.EmailRequest request, Map<String, Object> model) {
        try {
            Template template = freemarkerConfig.getTemplate(request.template());
            String htmlOutput = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            String[] templateParts = htmlOutput.split("-----");
            if (templateParts.length != 2) {
                log.error("Parsing error for template {}", request.template());
                throw new SendEmailException("An error occurred");
            }
            String subject = templateParts[0].trim();
            String body = templateParts[1].trim();

            return new EmailParts(subject, body);
        } catch (IOException | TemplateException e) {
            log.error("Error while generating email parts for template {} and recipient {}", request.template(), request.to(), e);
            throw new SendEmailException("Error while sending email to " + request.to());
        }
    }

}
