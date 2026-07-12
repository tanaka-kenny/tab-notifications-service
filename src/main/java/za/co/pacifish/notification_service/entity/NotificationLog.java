package za.co.pacifish.notification_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import za.co.pacifish.notification_service.enumeration.Channel;
import za.co.pacifish.notification_service.enumeration.NotificationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Channel channel;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationStatus status;

    @Column(columnDefinition = "TEXT")
    private String errorDetails;

    @CreationTimestamp
    private LocalDateTime createdAt;




}
