package za.co.pacifish.notification_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.pacifish.notification_service.dto.SendNotificationRequest;
import za.co.pacifish.notification_service.dto.SendNotificationResponse;
import za.co.pacifish.notification_service.service.NotificationService;

@RestController
@RequestMapping("api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<SendNotificationResponse> sendNotification(
        @RequestBody SendNotificationRequest request) {
        return ResponseEntity.ok(notificationService.sendNotification(request));
    }
}
