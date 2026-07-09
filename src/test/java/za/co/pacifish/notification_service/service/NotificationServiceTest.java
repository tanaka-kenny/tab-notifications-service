package za.co.pacifish.notification_service.service;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.pacifish.notification_service.dto.SendEmailRequest;
import za.co.pacifish.notification_service.entity.NotificationLog;
import za.co.pacifish.notification_service.enumeration.Channel;
import za.co.pacifish.notification_service.enumeration.EmailTemplate;
import za.co.pacifish.notification_service.enumeration.NotificationStatus;
import za.co.pacifish.notification_service.exception.SendEmailException;
import za.co.pacifish.notification_service.repository.NotificationLogRepository;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationLogRepository notificationsLogRepository;

    @Mock
    private EmailHelper springEmailHelper;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(notificationsLogRepository, springEmailHelper);
    }

    @Test
    void sendEmail_savesSuccessNotificationLogWhenEmailIsSent() throws MessagingException {
        SendEmailRequest payload = payload("recipient@example.com", Map.of("name", "Tanaka"));

        notificationService.sendEmail(payload);

        verify(springEmailHelper).sendEmail(payload.emailRequest(), payload.templateVariables());

        ArgumentCaptor<NotificationLog> logCaptor = ArgumentCaptor.forClass(NotificationLog.class);
        verify(notificationsLogRepository).save(logCaptor.capture());

        NotificationLog savedLog = logCaptor.getValue();
        assertEquals(Channel.EMAIL, savedLog.getChannel());
        assertEquals("recipient@example.com", savedLog.getRecipient());
        assertEquals(NotificationStatus.SUCCESS, savedLog.getStatus());
        assertNull(savedLog.getErrorDetails());

        verifyNoMoreInteractions(springEmailHelper, notificationsLogRepository);
    }

    @Test
    void sendEmail_savesFailedNotificationLogAndThrowsSendEmailExceptionWhenHelperFails() throws MessagingException {
        SendEmailRequest payload = payload("failure@example.com", Map.of("inviteLink", "https://example.com"));
        doThrow(new MessagingException("smtp failure"))
            .when(springEmailHelper)
            .sendEmail(payload.emailRequest(), payload.templateVariables());

        SendEmailException exception = assertThrows(
            SendEmailException.class,
            () -> notificationService.sendEmail(payload)
        );

        assertEquals("An error occurred while sending email to failure@example.com", exception.getMessage());

        verify(springEmailHelper).sendEmail(payload.emailRequest(), payload.templateVariables());

        ArgumentCaptor<NotificationLog> logCaptor = ArgumentCaptor.forClass(NotificationLog.class);
        verify(notificationsLogRepository).save(logCaptor.capture());

        NotificationLog savedLog = logCaptor.getValue();
        assertEquals(Channel.EMAIL, savedLog.getChannel());
        assertEquals("failure@example.com", savedLog.getRecipient());
        assertEquals(NotificationStatus.FAILED, savedLog.getStatus());
        assertTrue(savedLog.getErrorDetails().contains("smtp failure"));

        verifyNoMoreInteractions(springEmailHelper, notificationsLogRepository);
    }

    @Test
    void sendEmail_propagatesRuntimeSendEmailExceptionWithoutSavingLog() throws MessagingException {
        SendEmailRequest payload = payload("runtime@example.com", Map.of("name", "Tanaka"));
        doThrow(new SendEmailException("Error while sending email to runtime@example.com"))
            .when(springEmailHelper)
            .sendEmail(payload.emailRequest(), payload.templateVariables());

        SendEmailException exception = assertThrows(
            SendEmailException.class,
            () -> notificationService.sendEmail(payload)
        );

        assertEquals("Error while sending email to runtime@example.com", exception.getMessage());
        verify(springEmailHelper).sendEmail(payload.emailRequest(), payload.templateVariables());
        verifyNoInteractions(notificationsLogRepository);
        verifyNoMoreInteractions(springEmailHelper);
    }

    private SendEmailRequest payload(String recipient, Map<String, Object> variables) {
        SendEmailRequest.EmailRequest emailRequest = new SendEmailRequest.EmailRequest(recipient, EmailTemplate.TAB_PLATFORM_INVITE);
        return new SendEmailRequest(emailRequest, variables);
    }
}
