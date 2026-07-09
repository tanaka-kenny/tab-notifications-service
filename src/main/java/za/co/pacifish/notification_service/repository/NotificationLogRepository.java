package za.co.pacifish.notification_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.pacifish.notification_service.entity.NotificationLog;

public interface NotificationLogRepository extends JpaRepository<NotificationLog,Integer> {
}
