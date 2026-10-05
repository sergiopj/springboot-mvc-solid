package com.atlas.bank.atlas_bank.transaction.exception;

import java.math.BigDecimal;

// Excepción personalizada de negocio para fondos insuficientes.
// Hereda de RuntimeException (excepción no verificada) para no obligar a usar bloques try-catch
// en toda la aplicación y permitir que Spring haga rollback automático en métodos con @Transactional.
public class InsufficientFundsException extends RuntimeException {

    // Constructor que arma un mensaje descriptivo con los datos del error,
    // el cual será capturado por el GlobalExceptionHandler para devolver una respuesta HTTP adecuada.
    public InsufficientFundsException(Long accountId, BigDecimal amount, BigDecimal balance) {
        super("Insufficient funds in account with ID: " + accountId + ". Requested: " + amount + ", Available: "
                + balance);
    }
}
