package za.co.pacifish.notification_service.service;

import za.co.pacifish.notification_service.dto.NotificationRequest;
import za.co.pacifish.notification_service.entity.NotificationLog;
import za.co.pacifish.notification_service.enumeration.Channel;

public interface NotificationStrategy {
    Channel getSupportedChannel();
    NotificationLog execute(NotificationRequest request);
}
