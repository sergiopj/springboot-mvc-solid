package com.atlas.bank.atlas_bank.transaction.service.fee;

import java.math.BigDecimal;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.atlas.bank.atlas_bank.account.model.enums.AccountType;

// Estrategia de comisión para cuentas corrientes (Checking)
// @Component: Spring la registra y la inyecta automáticamente en List<FeeCalculator>
// @Order(1): Prioridad alta para evaluarse antes que DefaultFeeCalculator
@Component
@Order(1)
public class CheckingFeeCalculator implements FeeCalculator {

    // Aplica solo si la cuenta es de tipo CHECKING
    @Override
    public boolean supports(String accountType) {
        return AccountType.CHECKING.name().equals(accountType);
    }

    // Calcula el 1.5% de comisión sobre el monto transferido
    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(new BigDecimal("0.015"));
    }
}