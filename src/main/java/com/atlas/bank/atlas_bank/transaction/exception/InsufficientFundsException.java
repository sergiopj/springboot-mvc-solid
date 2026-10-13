package com.atlas.bank.atlas_bank.transaction.exception;

import java.math.BigDecimal;

// Excepción lanzada cuando una cuenta no tiene saldo suficiente para la transferencia
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(Long accountId, BigDecimal amount, BigDecimal balance) {
        super("Insufficient funds in account with ID: " + accountId + ". Requested: " + amount + ", Available: "
                + balance);
    }
}
