package za.co.pacifish.notification_service.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import za.co.pacifish.notification_service.config.SendGridProperties;
import za.co.pacifish.notification_service.config.TabServicesProperties;
import za.co.pacifish.notification_service.dto.GetCustomerResponse;
import za.co.pacifish.notification_service.dto.SendNotificationRequest;
import za.co.pacifish.notification_service.dto.SendNotificationResponse;
import za.co.pacifish.notification_service.entity.Notification;
import za.co.pacifish.notification_service.enumeration.NotificationStatus;
import za.co.pacifish.notification_service.repository.NotificationRepository;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final TabServicesProperties tabServicesProperties;
    private final SendGridProperties sendGridProperties;
    private final RestClient restClient;

    public SendNotificationResponse sendNotification(SendNotificationRequest request) {
        try {
            GetCustomerResponse getCustomerResponse = restClient.get()
                .uri(tabServicesProperties.getRegistrationBaseUrl() + "/api/web/customers/" + request.customerId())
                .retrieve()
                .body(GetCustomerResponse.class);

            if (getCustomerResponse == null) {
                throw new UnsupportedOperationException("Received null response from get customer endpoint.");
            }

            Notification notification = Notification.builder()
                .uuid(UUID.randomUUID().toString())
                .customerFirebaseUid(request.customerId())
                .subject(request.subject())
                .body(request.body())
                .notificationType(request.notificationType())
                .status(NotificationStatus.PENDING)
                .build();
            notificationRepository.save(notification);
            log.info("Notification created with uuid: {}", notification.getUuid());

            sendEmail(getCustomerResponse.email(), request.subject(), request.body());
            notification.setStatus(NotificationStatus.SENT);
            notificationRepository.save(notification);
            log.info("Notification with uuid: {} sent successfully.", notification.getUuid());

            return SendNotificationResponse.builder()
                .notificationUuid(notification.getUuid())
                .status(notification.getStatus())
                .build();

        } catch (RestClientException ex) {
            log.error("Error fetching customer details: {}", ex.getMessage());
            throw new UnsupportedOperationException("Failed to fetch customer details.");
        }
    }

    private void sendEmail(@jakarta.validation.constraints.Email String to, String subject, String body) {
        Email from = new Email(sendGridProperties.getFromEmail());
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, new Email(to), content);

        SendGrid sg = new SendGrid(sendGridProperties.getApiKey());
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            log.debug(String.valueOf(response.getStatusCode()));
            log.debug(response.getBody());
            log.debug(response.getHeaders().toString());
        } catch (IOException ex) {
            log.error("Error sending email: {}", ex.getMessage());
            throw new UnsupportedOperationException("Failed to send email.");
        }
    }
}
