package za.co.pacifish.notification_service.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("EMAIL")
public class EmailLog extends NotificationLog {

    // todo: Add email provider specific logs when integrated into Send Grid
}
