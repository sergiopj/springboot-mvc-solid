package com.atlas.bank.atlas_bank.transaction.validation.chain;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.atlas.bank.atlas_bank.account.exception.AccountNotActiveException;
import com.atlas.bank.atlas_bank.account.model.enums.AccountStatus;
import com.atlas.bank.atlas_bank.transaction.service.transfer.TransferContext;

import lombok.RequiredArgsConstructor;

@Component
@Order(1)
@RequiredArgsConstructor
public class AccountStatusValidator implements TransferValidator {

    @Override
    public void validate(TransferContext context) {
        if (context.sourceAccount().getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(context.sourceAccount().getStatus(),
                    context.sourceAccount().getId());
        }

        if (context.targetAccount().getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(context.targetAccount().getStatus(),
                    context.targetAccount().getId());
        }
    }

}
