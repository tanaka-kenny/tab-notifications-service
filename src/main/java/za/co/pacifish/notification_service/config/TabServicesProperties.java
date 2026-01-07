package za.co.pacifish.notification_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tab-services")
@Data
public class TabServicesProperties {
    private String registrationBaseUrl;
}
