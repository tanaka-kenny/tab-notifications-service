package za.co.pacifish.notification_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import za.co.pacifish.notification_service.enumeration.Channel;

import java.util.Map;

public record NotificationRequest(
    @NotNull(message = "Channel type is required (e.g., EMAIL, SMS)")
    Channel channel,
    @NotBlank(message = "Recipient identifier is required")
    String recipient,
    @NotBlank(message = "Template is required")
    String template,
    @NotNull(message = "Variables map cannot be null")
    Map<String, Object> variables ) {

}
