package com.atlas.bank.atlas_bank.shared.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.atlas.bank.atlas_bank.account.exception.AccountNotActiveException;
import com.atlas.bank.atlas_bank.account.exception.AccountNotFoundException;
import com.atlas.bank.atlas_bank.transaction.exception.InsufficientFundsException;

// Excepcion GLOBAL para manejar errores de la aplicacion, en este caso cuando no se encuentra una cuenta.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja la excepcion AccountNotFoundException y devuelve un ProblemDetail con
    // el estado HTTP 404 y un mensaje de error.
    @ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFoundException(AccountNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Account Not Found");
        return problemDetail;
    }

    @ExceptionHandler(AccountNotActiveException.class)
    public ProblemDetail handleAccountNotActiveException(AccountNotActiveException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(422), ex.getMessage());
        problemDetail.setTitle("Account Not Active");
        return problemDetail;
    }

    // Maneja la excepcion InsufficientFundsException y devuelve un ProblemDetail
    // con el estado HTTP 422 y un mensaje de error.
    @ExceptionHandler(InsufficientFundsException.class)
    public ProblemDetail handleInsufficientFundsException(InsufficientFundsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(422), ex.getMessage());
        problemDetail.setTitle("Insufficient Funds");
        return problemDetail;
    }

    // Maneja cualquier otra excepcion no controlada y devuelve un ProblemDetail con
    // el estado HTTP 500 y un mensaje de error generico. muy importante para no
    // exponer detalles internos de la aplicacion al cliente.
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        problemDetail.setTitle("General Error");
        return problemDetail;
    }

    // Maneja la excepcion MethodArgumentNotValidException y devuelve un
    // ProblemDetail con
    // el estado HTTP 400 y un mensaje de error que contiene los errores de
    // validacion
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(
                HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Method Argument Not Valid");

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

}
