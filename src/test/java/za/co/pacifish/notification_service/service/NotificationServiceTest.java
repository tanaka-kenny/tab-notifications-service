package za.co.pacifish.notification_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.pacifish.notification_service.dto.NotificationRequest;
import za.co.pacifish.notification_service.entity.NotificationLog;
import za.co.pacifish.notification_service.enumeration.Channel;
import za.co.pacifish.notification_service.enumeration.NotificationStatus;
import za.co.pacifish.notification_service.exception.SendNotificationException;
import za.co.pacifish.notification_service.repository.NotificationLogRepository;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationFactory factory;

    @Mock
    private NotificationLogRepository notificationLogRepository;

    @Mock
    private NotificationStrategy notificationStrategy;

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(factory, notificationLogRepository);
    }

    @Test
    void dispatchNotification_savesSuccessNotificationLogWhenStrategySucceeds() {
        NotificationRequest request = payload(Channel.EMAIL, "recipient@example.com", "invite.ftl", Map.of("name", "Tanaka"));
        NotificationLog successfulLog = NotificationLog.builder()
            .recipient(request.recipient())
            .templateKey(request.template())
            .status(NotificationStatus.SUCCESS)
            .build();

        when(factory.getStrategy(Channel.EMAIL)).thenReturn(notificationStrategy);
        when(notificationStrategy.execute(request)).thenReturn(successfulLog);

        NotificationLog result = notificationService.dispatchNotification(request);
        assertEquals(successfulLog, result);

        ArgumentCaptor<NotificationLog> logCaptor = ArgumentCaptor.forClass(NotificationLog.class);
        verify(notificationLogRepository).save(logCaptor.capture());
        NotificationLog savedLog = logCaptor.getValue();

        assertEquals("recipient@example.com", savedLog.getRecipient());
        assertEquals(NotificationStatus.SUCCESS, savedLog.getStatus());
        assertNull(savedLog.getErrorDetails());

        verify(factory).getStrategy(Channel.EMAIL);
        verify(notificationStrategy).execute(request);
        verifyNoMoreInteractions(factory, notificationStrategy, notificationLogRepository);
    }

    @Test
    void dispatchNotification_savesFailedNotificationLogAndThrowsWhenStrategyFails() {
        NotificationRequest request = payload(Channel.EMAIL, "failure@example.com", "invite.ftl", Map.of("inviteLink", "https://example.com"));
        NotificationLog failedLog = NotificationLog.builder()
            .recipient(request.recipient())
            .templateKey(request.template())
            .status(NotificationStatus.FAILED)
            .errorDetails("smtp failure")
            .build();

        when(factory.getStrategy(Channel.EMAIL)).thenReturn(notificationStrategy);
        when(notificationStrategy.execute(request)).thenReturn(failedLog);

        SendNotificationException exception = assertThrows(
            SendNotificationException.class,
            () -> notificationService.dispatchNotification(request)
        );

        assertEquals("Failed to send notification", exception.getMessage());

        ArgumentCaptor<NotificationLog> logCaptor = ArgumentCaptor.forClass(NotificationLog.class);
        verify(notificationLogRepository).save(logCaptor.capture());

        NotificationLog savedLog = logCaptor.getValue();
        assertEquals("failure@example.com", savedLog.getRecipient());
        assertEquals(NotificationStatus.FAILED, savedLog.getStatus());
        assertTrue(savedLog.getErrorDetails().contains("smtp failure"));

        verify(factory).getStrategy(Channel.EMAIL);
        verify(notificationStrategy).execute(request);
        verifyNoMoreInteractions(factory, notificationStrategy, notificationLogRepository);
    }

    @Test
    void dispatchNotification_propagatesFactoryExceptionWithoutSavingLog() {
        NotificationRequest request = payload(Channel.PUSH_NOTIFICATION, "device-id-1", "push.ftl", Map.of("name", "Tanaka"));
        when(factory.getStrategy(Channel.PUSH_NOTIFICATION))
            .thenThrow(new IllegalArgumentException("No strategy found for channel: PUSH_NOTIFICATION"));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> notificationService.dispatchNotification(request)
        );

        assertEquals("No strategy found for channel: PUSH_NOTIFICATION", exception.getMessage());
        verify(factory).getStrategy(Channel.PUSH_NOTIFICATION);
        verifyNoInteractions(notificationStrategy, notificationLogRepository);
        verifyNoMoreInteractions(factory);
    }

    private NotificationRequest payload(
        Channel channel,
        String recipient,
        String template,
        Map<String, Object> variables
    ) {
        return new NotificationRequest(channel, recipient, template, variables);
    }
}
