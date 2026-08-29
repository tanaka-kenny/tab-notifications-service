package za.co.pacifish.notification_service.exception;

public class SendNotificationException extends RuntimeException {
    public SendNotificationException(String message) {
        super(message);
    }
}
