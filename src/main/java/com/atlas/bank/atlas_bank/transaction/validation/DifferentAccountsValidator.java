package com.atlas.bank.atlas_bank.transaction.validation;

import com.atlas.bank.atlas_bank.transaction.dto.TransferRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Validador que comprueba que la cuenta de origen y destino no coincidan
public class DifferentAccountsValidator implements ConstraintValidator<DifferentAccounts, TransferRequest> {

    @Override
    public boolean isValid(TransferRequest request, ConstraintValidatorContext context) {
        if (request.getSourceAccountId() == null && request.getTargetAccountId() == null) {
            return true;
        }
        return !request.getSourceAccountId().equals(request.getTargetAccountId());
    }
}