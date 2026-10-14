package com.atlas.bank.atlas_bank.account.exception;

import com.atlas.bank.atlas_bank.account.model.enums.AccountStatus;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException(AccountStatus status, Long accountId) {
        super("Account with ID: " + accountId + " is not active. Current status: " + status);
    }

}
