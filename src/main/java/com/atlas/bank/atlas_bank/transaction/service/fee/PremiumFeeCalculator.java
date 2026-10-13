package com.atlas.bank.atlas_bank.transaction.service.fee;

import java.math.BigDecimal;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.atlas.bank.atlas_bank.account.model.enums.AccountType;

// Estrategia de comisión para cuentas Premium
// @Component: Spring la registra y la inyecta automáticamente en List<FeeCalculator>
// @Order(1): Prioridad alta para evaluarse antes que DefaultFeeCalculator
@Component
@Order(1)
public class PremiumFeeCalculator implements FeeCalculator {

    // Aplica solo si la cuenta es de tipo PREMIUM
    @Override
    public boolean supports(String accountType) {
        return AccountType.PREMIUM.name().equals(accountType);
    }

    // Cuentas premium no pagan comisión (0)
    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return BigDecimal.ZERO;
    }
}
