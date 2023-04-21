package com.kuehnenagel.citylist.common.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handleException(ServiceException ex) {
        log.debug("Sending error response '{}'", ex.getError());
        return ResponseEntity.status(ex.getError().getStatus()).body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedExceptionException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value())
                .body(ServiceError.NO_PERMISSIONS.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error("Sending error response - unknown exception occurred:", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }
}
