package org.example.socks.exception;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.example.socks.dto.exception.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Hidden
@RestControllerAdvice()
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SocksNotEnoughException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleNotEnoughSocksException() {}

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

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationException(ConstraintViolationException ex) {
        return new ErrorResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
