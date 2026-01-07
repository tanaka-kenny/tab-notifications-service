package za.co.pacifish.notification_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sendgrid")
@Data
public class SendGridProperties {
    private String apiKey;
    private String fromEmail;
}
