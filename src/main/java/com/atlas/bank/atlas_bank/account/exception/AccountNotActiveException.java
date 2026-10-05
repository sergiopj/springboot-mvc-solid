package com.atlas.bank.atlas_bank.account.exception;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException(String status, Long accountId) {
        super("Account with ID: " + accountId + " is not active. Current status: " + status);
    }

}
