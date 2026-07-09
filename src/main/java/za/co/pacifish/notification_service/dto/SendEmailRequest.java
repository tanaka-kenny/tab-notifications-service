package za.co.pacifish.notification_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import za.co.pacifish.notification_service.enumeration.EmailTemplate;

import java.util.Map;

public record SendEmailRequest(
    @NotNull EmailRequest emailRequest,
    @NotNull Map<String, Object> templateVariables
    ) {

    public record EmailRequest(
        @Pattern(regexp = ".+@.+\\..+", message = "Invalid email format") String to,
        @NotNull(message = "Email template must not be null") EmailTemplate template
    ) {
    }
}
