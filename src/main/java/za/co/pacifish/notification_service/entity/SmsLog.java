package za.co.pacifish.notification_service.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SMS")
public class SmsLog extends NotificationLog {

    // todo: Add SMS provider specific logs when integrated into Twilio/Bulk SMS
}
