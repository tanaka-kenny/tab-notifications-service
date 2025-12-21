package za.co.pacifish.notification_service.dto;

public record FirebaseUserDetailsDto(
    String email,
    String firebaseUid
) {
}