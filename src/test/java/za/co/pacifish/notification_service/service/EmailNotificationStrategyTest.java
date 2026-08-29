package za.co.pacifish.notification_service.service;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import za.co.pacifish.notification_service.dto.NotificationRequest;
import za.co.pacifish.notification_service.entity.NotificationLog;
import za.co.pacifish.notification_service.enumeration.Channel;
import za.co.pacifish.notification_service.enumeration.NotificationStatus;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EmailNotificationStrategyTest {

    @Test
    void execute_setsSuccessStatusWhenEmailIsSent() throws MessagingException {
        EmailHelper emailHelper = mock(EmailHelper.class);
        EmailNotificationStrategy strategy = new EmailNotificationStrategy(emailHelper);
        NotificationRequest request = payload("recipient@example.com", Map.of("name", "Tanaka"));

        NotificationLog result = strategy.execute(request);

        verify(emailHelper).sendEmail(request);
        assertEquals("recipient@example.com", result.getRecipient());
        assertEquals("invite.ftl", result.getTemplateKey());
        assertEquals(NotificationStatus.SUCCESS, result.getStatus());
        assertNull(result.getErrorDetails());
    }

    @Test
    void execute_setsFailedStatusAndErrorDetailsWhenEmailSendingFails() throws MessagingException {
        EmailHelper emailHelper = mock(EmailHelper.class);
        EmailNotificationStrategy strategy = new EmailNotificationStrategy(emailHelper);
        NotificationRequest request = payload("failure@example.com", Map.of("name", "Tanaka"));
        doThrow(new MessagingException("smtp failure"))
            .when(emailHelper)
            .sendEmail(request);

        NotificationLog result = strategy.execute(request);

        verify(emailHelper).sendEmail(request);
        assertEquals("failure@example.com", result.getRecipient());
        assertEquals("invite.ftl", result.getTemplateKey());
        assertEquals(NotificationStatus.FAILED, result.getStatus());
        assertTrue(result.getErrorDetails().contains("smtp failure"));
    }

    private NotificationRequest payload(String recipient, Map<String, Object> variables) {
        return new NotificationRequest(Channel.EMAIL, recipient, "invite.ftl", variables);
    }
}
