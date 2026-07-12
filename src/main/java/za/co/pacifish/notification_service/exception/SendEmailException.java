package za.co.pacifish.notification_service.exception;

public class SendEmailException extends RuntimeException {
    public SendEmailException(String message) {
        super(message);
    }
}
