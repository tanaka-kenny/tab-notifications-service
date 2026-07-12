package za.co.pacifish.notification_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.pacifish.notification_service.dto.SendEmailRequest;
import za.co.pacifish.notification_service.service.NotificationService;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationsController {

    private final NotificationService notificationService;

    @PostMapping("/email")
    public ResponseEntity<Object> sendEmail(
        @Valid @RequestBody SendEmailRequest payload) {
        notificationService.sendEmail(payload);
        return ResponseEntity.ok().build();
    }


}
