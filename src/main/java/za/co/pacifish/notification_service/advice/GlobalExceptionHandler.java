package za.co.pacifish.notification_service.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import za.co.pacifish.notification_service.exception.SendEmailException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SendEmailException.class)
    public ResponseEntity<Object> handleSendEmailException(SendEmailException e) {
        ProblemDetail detail
            = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.of(detail).build();
    }
}
