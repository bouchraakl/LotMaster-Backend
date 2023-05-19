package br.com.uniamerica.estacionamento.config;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(
            final MethodArgumentNotValidException methodArgumentNotValidException
    ){
        final Map<String, String> errors = new HashMap<>();

        methodArgumentNotValidException
                .getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    errors.put(
                            ((FieldError) error).getField(),
                            error.getDefaultMessage());
                });

        return errors;
    }
}
