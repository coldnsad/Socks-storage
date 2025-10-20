package org.example.socks.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.example.socks.dto.exception.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Hidden
@RestControllerAdvice()
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SocksNotEnoughException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleNotEnoughSocksException() {
    }

    @ExceptionHandler(WrongExcelFileException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponseDto handleWrongExcelFileException(Exception ex) {
        return new ErrorResponseDto(ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(SocksNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleNotFoundSocksException(Exception ex) {
        return new ErrorResponseDto(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    //MethodArgumentNotValidException выбрасывается в при использовании @Valid @Validated в контроллере
    //ConstraintViolationException выбрасывается в при использовании @Valid @Validated в сервисе/на уровне hibernate
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationException(ConstraintViolationException ex) {
        return new ErrorResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               @Nullable HttpHeaders headers,
                                                               @Nullable HttpStatusCode status,
                                                               @Nullable WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
