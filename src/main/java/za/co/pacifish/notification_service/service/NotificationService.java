package za.co.pacifish.notification_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.pacifish.notification_service.dto.NotificationRequest;
import za.co.pacifish.notification_service.entity.NotificationLog;
import za.co.pacifish.notification_service.enumeration.NotificationStatus;
import za.co.pacifish.notification_service.exception.SendNotificationException;
import za.co.pacifish.notification_service.repository.NotificationLogRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationFactory factory;
    private final NotificationLogRepository notificationLogRepository;

    @Transactional
    public NotificationLog dispatchNotification(NotificationRequest request) {
        NotificationStrategy notificationStrategy = factory.getStrategy(request.channel());
        NotificationLog notification = notificationStrategy.execute(request);

        notificationLogRepository.save(notification);

        if (notification.getStatus() == NotificationStatus.FAILED) {
            throw new SendNotificationException("Failed to send notification");
        }

        return notification;
    }

}
