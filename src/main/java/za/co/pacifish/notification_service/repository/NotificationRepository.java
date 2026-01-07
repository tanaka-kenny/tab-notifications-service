package za.co.pacifish.notification_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.pacifish.notification_service.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
