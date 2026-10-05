package com.atlas.bank.atlas_bank.transaction.validation;

import com.atlas.bank.atlas_bank.transaction.dto.TransferRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Aquí vive la lógica real de @DifferentAccounts
// Es como el .validate() de tu decorador custom en NestJS
public class DifferentAccountsValidator implements ConstraintValidator<DifferentAccounts, TransferRequest> {

    @Override
    public boolean isValid(TransferRequest request, ConstraintValidatorContext context) {
        // Si ambos son nulos, no hay nada que validar, lo dejamos pasar
        if (request.getSourceAccountId() == null && request.getTargetAccountId() == null) {
            return true;
        }

        // VALIDACION Regla de negocio Atlas-Bank: no puedes transferirte a ti mismo
        return !request.getSourceAccountId().equals(request.getTargetAccountId());
    }
}