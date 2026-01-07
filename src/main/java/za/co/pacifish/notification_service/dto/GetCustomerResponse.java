package za.co.pacifish.notification_service.dto;

public record GetCustomerResponse(
    String firstName,
    String lastName,
    String email,
    String phoneNumber
) {
}
