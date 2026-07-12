package za.co.pacifish.notification_service.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.pacifish.notification_service.dto.SendEmailRequest;
import za.co.pacifish.notification_service.entity.NotificationLog;
import za.co.pacifish.notification_service.enumeration.Channel;
import za.co.pacifish.notification_service.enumeration.NotificationStatus;
import za.co.pacifish.notification_service.exception.SendEmailException;
import za.co.pacifish.notification_service.repository.NotificationLogRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationLogRepository notificationsLogRepository;
    private final EmailHelper springEmailHelper;

    public void sendEmail(SendEmailRequest payload) {

        NotificationLog notificationLog = NotificationLog.builder()
            .channel(Channel.EMAIL)
            .recipient(payload.emailRequest().to())
            .build();

        try {
            springEmailHelper.sendEmail(payload.emailRequest(), payload.templateVariables());

            log.info("Email sent to {}", payload.emailRequest().to());

            notificationLog.setStatus(NotificationStatus.SUCCESS);
            notificationsLogRepository.save(notificationLog);
        } catch (MessagingException ex) {
            log.error("Error while sending email to {}", payload.emailRequest().to(), ex);
            notificationLog.setErrorDetails(String.valueOf(ex));
            notificationLog.setStatus(NotificationStatus.FAILED);
            notificationsLogRepository.save(notificationLog);

            throw new SendEmailException("An error occurred while sending email to " + payload.emailRequest().to());

        }
    }

}
