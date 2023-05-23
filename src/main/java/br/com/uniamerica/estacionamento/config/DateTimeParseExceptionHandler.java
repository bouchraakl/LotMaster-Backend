package br.com.uniamerica.estacionamento.config;

import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DateTimeParseExceptionHandler {

    /**
     * Handles the DateTimeParseException and returns a ResponseEntity with an error message.
     *
     * @param ex The DateTimeParseException to handle.
     * @return ResponseEntity with an error message and HttpStatus.BAD_REQUEST.
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleException(DateTimeParseException ex) {
        String errorMessage = "Falha ao analisar a data e hora. " +
                "Por favor, forneça a data e hora no formato 'yyyy-MM-dd'T'HH:mm:ss'.";
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
