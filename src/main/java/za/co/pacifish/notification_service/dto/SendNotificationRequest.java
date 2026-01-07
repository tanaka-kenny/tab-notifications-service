package za.co.pacifish.notification_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import za.co.pacifish.notification_service.enumeration.NotificationType;

public record SendNotificationRequest(
    @NotBlank String customerId,
    @NonNull NotificationType notificationType,
    @NotBlank String subject,
    @NotBlank String body
) {
}
