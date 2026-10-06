package com.atlas.bank.atlas_bank.shared.exception;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.atlas.bank.atlas_bank.account.exception.AccountNotFoundException;
import com.atlas.bank.atlas_bank.account.exception.AccountNotActiveException;
import com.atlas.bank.atlas_bank.transaction.exception.InsufficientFundsException;

import lombok.extern.slf4j.Slf4j;

// Filtro global de errores. Si en cualquier Controller o Service lanzas una excepción, cae aquí.
// Convierte la excepción en un JSON estándar (RFC 7807 ProblemDetail) para el cliente.
// En NestJS sería @Catch() global.
// Genera un logger SLF4J ('log') para registrar trazas y errores en consola.
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - La cuenta no existe
    @ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFoundException(AccountNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Account Not Found");
        return problemDetail;
    }

    // 422 - La cuenta existe pero está bloqueada/cerrada. Es error de negocio, no
    // de datos.
    @ExceptionHandler(AccountNotActiveException.class)
    public ProblemDetail handleAccountNotActiveException(AccountNotActiveException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT,
                ex.getMessage());
        problemDetail.setTitle("Account Not Active");
        return problemDetail;
    }

    // 422 - No hay saldo. También es error de negocio.
    @ExceptionHandler(InsufficientFundsException.class)
    public ProblemDetail handleInsufficientFundsException(InsufficientFundsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT,
                ex.getMessage());
        problemDetail.setTitle("Insufficient Funds");
        return problemDetail;
    }

    // 400 - Falló @Valid del DTO (ej: @NotNull, @DifferentAccounts)
    // Aquí llegan los errores de Bean Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation Failed");

        List<String> errors = new ArrayList<>();
        // Errores de campos: ej "amount must be positive"
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));
        // Errores de clase: ej "Source and target accounts must be different"
        ex.getBindingResult().getGlobalErrors().forEach(error -> errors.add(error.getDefaultMessage()));

        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    // 500 - Cualquier error no controlado. Registramos el error en consola para poder depurar
    // y devolvemos un mensaje genérico seguro al cliente.
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        log.error("Error no controlado en la aplicación: ", ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        problemDetail.setTitle("Unexpected Error");
        return problemDetail;
    }
}