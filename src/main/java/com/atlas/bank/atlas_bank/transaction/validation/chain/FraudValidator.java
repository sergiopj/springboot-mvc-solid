package com.atlas.bank.atlas_bank.transaction.validation.chain;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.atlas.bank.atlas_bank.transaction.service.exception.FraudCheckException;
import com.atlas.bank.atlas_bank.transaction.service.fraud.FraudCheckResult;
import com.atlas.bank.atlas_bank.transaction.service.fraud.FraudChecker;
import com.atlas.bank.atlas_bank.transaction.service.transfer.TransferContext;

@Component
@Order(3)
public class FraudValidator implements TransferValidator {

    private final FraudChecker fraudChecker;

    public FraudValidator(FraudChecker fraudChecker) {
        this.fraudChecker = fraudChecker;
    }

    @Override
    public void validate(TransferContext context) {
        FraudCheckResult fraudCheckResult = fraudChecker.check(context.sourceAccount().getId(), context.amount());
        if (fraudCheckResult.blocked()) {
            throw new FraudCheckException(fraudCheckResult.reason());
        }
    }
}
