package com.atlas.bank.atlas_bank.shared.exception;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.atlas.bank.atlas_bank.account.exception.AccountNotFoundException;
import com.atlas.bank.atlas_bank.account.exception.AccountNotActiveException;
import com.atlas.bank.atlas_bank.transaction.exception.InsufficientFundsException;

import lombok.extern.slf4j.Slf4j;

// Manejador global de excepciones para transformar errores en respuestas RFC 7807 (ProblemDetail)
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Cuenta no encontrada
    @ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFoundException(AccountNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Account Not Found");
        return problemDetail;
    }

    // 422 - Cuenta inactiva o bloqueada
    @ExceptionHandler(AccountNotActiveException.class)
    public ProblemDetail handleAccountNotActiveException(AccountNotActiveException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT,
                ex.getMessage());
        problemDetail.setTitle("Account Not Active");
        return problemDetail;
    }

    // 422 - Fondos insuficientes para la transferencia
    @ExceptionHandler(InsufficientFundsException.class)
    public ProblemDetail handleInsufficientFundsException(InsufficientFundsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT,
                ex.getMessage());
        problemDetail.setTitle("Insufficient Funds");
        return problemDetail;
    }

    // 400 - Fallos de validación en DTOs (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation Failed");

        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors().forEach(error -> errors.add(error.getDefaultMessage()));

        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    // 409 - Conflicto por restricción única en BD (ej: número de cuenta duplicado)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.warn("Violación de integridad de datos: {}", ex.getMostSpecificCause().getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, "Account number already exists or violates a unique constraint.");
        problemDetail.setTitle("Conflict");
        return problemDetail;
    }

    // 500 - Errores no controlados
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        log.error("Error no controlado en la aplicación: ", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        problemDetail.setTitle("Unexpected Error");
        return problemDetail;
    }
}