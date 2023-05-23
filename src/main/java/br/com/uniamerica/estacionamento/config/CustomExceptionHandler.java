package br.com.uniamerica.estacionamento.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
        String errorMessage = "Falha ao analisar a data e hora." +
                " Por favor, forneça a data e hora no formato 'yyyy-MM-dd'T'HH:mm:ss'.";
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

}

