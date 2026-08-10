package za.co.pacifish.notification_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.pacifish.notification_service.dto.NotificationRequest;
import za.co.pacifish.notification_service.entity.NotificationLog;
import za.co.pacifish.notification_service.enumeration.NotificationStatus;
import za.co.pacifish.notification_service.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationsController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<Object> sendEmail(
        @Valid @RequestBody NotificationRequest payload) {
        NotificationLog notificationLog = notificationService.dispatchNotification(payload);
        return ResponseEntity.ok(notificationLog);
    }
}
