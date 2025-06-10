package ru.alexeev.etflux.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.alexeev.etflux.exception.dto.ErrorDto;

@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(InstrumentsNotFoundException.class)
    public ResponseEntity<ErrorDto> handleInstrumentsNotFoundException(InstrumentsNotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorDto.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.name())
                .path(request.getContextPath())
                .build(), HttpStatus.NOT_FOUND);
    }
}
