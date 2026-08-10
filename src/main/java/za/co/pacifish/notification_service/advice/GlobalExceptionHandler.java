package za.co.pacifish.notification_service.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import za.co.pacifish.notification_service.exception.SendNotificationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SendNotificationException.class)
    public ResponseEntity<Object> handleSendEmailException(SendNotificationException e) {
        ProblemDetail detail
            = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.of(detail).build();
    }
}
