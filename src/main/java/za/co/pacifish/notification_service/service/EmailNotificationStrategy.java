package za.co.pacifish.notification_service.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import za.co.pacifish.notification_service.dto.NotificationRequest;
import za.co.pacifish.notification_service.entity.NotificationLog;
import za.co.pacifish.notification_service.enumeration.Channel;
import za.co.pacifish.notification_service.enumeration.NotificationStatus;

@Component
@RequiredArgsConstructor
@Slf4j
class EmailNotificationStrategy implements NotificationStrategy {
    private final EmailHelper springEmailHelper;

    @Override
    public Channel getSupportedChannel() {
        return Channel.EMAIL;
    }

    @Override
    public NotificationLog execute(NotificationRequest request) {
        NotificationLog notificationLog = NotificationLog.builder()
            .recipient(request.recipient())
            .templateKey(request.template())
            .build();

        try {
            springEmailHelper.sendEmail(request);

            log.info("Email sent to {}", request.recipient());

            notificationLog.setStatus(NotificationStatus.SUCCESS);
        } catch (MessagingException ex) {
            log.error("Error while sending email to {}", request.recipient(), ex);
            notificationLog.setErrorDetails(String.valueOf(ex));
            notificationLog.setStatus(NotificationStatus.FAILED);
        }

        return notificationLog;
    }
}
