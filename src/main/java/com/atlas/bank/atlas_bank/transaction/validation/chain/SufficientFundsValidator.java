package com.atlas.bank.atlas_bank.transaction.validation.chain;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.atlas.bank.atlas_bank.transaction.exception.InsufficientFundsException;
import com.atlas.bank.atlas_bank.transaction.service.transfer.TransferContext;

@Component
@Order(2)
public class SufficientFundsValidator implements TransferValidator {
    @Override
    public void validate(TransferContext context) {
        if (context.sourceAccount().getBalance().compareTo(context.amount()) < 0) {
            throw new InsufficientFundsException(context.sourceAccount().getId(), context.amount(),
                    context.sourceAccount().getBalance());
        }
    }

}
