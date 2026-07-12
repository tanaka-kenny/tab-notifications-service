package za.co.pacifish.notification_service.service;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import za.co.pacifish.notification_service.dto.SendEmailRequest;
import za.co.pacifish.notification_service.exception.SendEmailException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmailHelperTest {

    @Test
    void sendEmail_sendsMimeMessageWithParsedSubjectAndBody() throws Exception {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        Configuration configuration = templateConfiguration("Welcome ${name}-----<p>Hello ${name}</p>");
        EmailHelper helper = new EmailHelper(mailSender, configuration);

        SendEmailRequest.EmailRequest request = new SendEmailRequest.EmailRequest(
            "recipient@example.com",
            EmailTemplate.TAB_PLATFORM_INVITE
        );

        helper.sendEmail(request, Map.of("name", "Tanaka"));

        assertEquals("Welcome Tanaka", mimeMessage.getSubject());
        assertEquals("recipient@example.com", mimeMessage.getAllRecipients()[0].toString());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mimeMessage.writeTo(out);
        String rawEmail = out.toString(StandardCharsets.UTF_8);
        assertTrue(rawEmail.contains("<p>Hello Tanaka</p>"));

        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendEmail_throwsWhenTemplateOutputCannotBeSplitIntoSubjectAndBody() {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        Configuration configuration = templateConfiguration("No separator in this output");
        EmailHelper helper = new EmailHelper(mailSender, configuration);

        SendEmailRequest.EmailRequest request = new SendEmailRequest.EmailRequest(
            "recipient@example.com",
            EmailTemplate.TAB_PLATFORM_INVITE
        );

        SendEmailException exception = assertThrows(
            SendEmailException.class,
            () -> helper.sendEmail(request, Map.of())
        );

        assertEquals("An error occurred", exception.getMessage());
        verify(mailSender, never()).send(mimeMessage);
    }

    @Test
    void sendEmail_throwsRecipientAwareErrorWhenTemplateLookupFails() throws IOException {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        Configuration configuration = mock(Configuration.class);
        when(configuration.getTemplate("tab-invitation-email.ftl")).thenThrow(new IOException("template missing"));

        EmailHelper helper = new EmailHelper(mailSender, configuration);

        SendEmailRequest.EmailRequest request = new SendEmailRequest.EmailRequest(
            "recipient@example.com",
            EmailTemplate.TAB_PLATFORM_INVITE
        );

        SendEmailException exception = assertThrows(
            SendEmailException.class,
            () -> helper.sendEmail(request, Map.of())
        );

        assertEquals("Error while sending email to recipient@example.com", exception.getMessage());
        verify(mailSender, never()).send(mimeMessage);
    }

    private Configuration templateConfiguration(String templateContents) {
        StringTemplateLoader loader = new StringTemplateLoader();
        loader.putTemplate("tab-invitation-email.ftl", templateContents);

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_33);
        configuration.setTemplateLoader(loader);
        return configuration;
    }
}
