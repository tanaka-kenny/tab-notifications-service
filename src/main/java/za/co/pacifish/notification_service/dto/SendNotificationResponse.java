package za.co.pacifish.notification_service.dto;

import lombok.Builder;
import za.co.pacifish.notification_service.enumeration.NotificationStatus;

@Builder
public record SendNotificationResponse(
    String notificationUuid,
    NotificationStatus status
) {
}
