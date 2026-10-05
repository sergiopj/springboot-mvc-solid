package com.atlas.bank.atlas_bank.transaction.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(Long accountId, BigDecimal amount, BigDecimal balance) {
        super("Insufficient funds in account with ID: " + accountId + ". Requested: " + amount + ", Available: "
                + balance);
    }

}
