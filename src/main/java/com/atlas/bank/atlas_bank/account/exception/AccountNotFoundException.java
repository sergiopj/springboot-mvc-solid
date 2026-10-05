package com.atlas.bank.atlas_bank.account.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super("Account not found with ID: " + id);
    }

}
