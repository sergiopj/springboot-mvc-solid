package com.atlas.bank.atlas_bank.transaction.service.fee;

import java.math.BigDecimal;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.atlas.bank.atlas_bank.account.model.enums.AccountType;

// Estrategia por defecto (Fallback) cuando ningún otro calculador reconoce la cuenta
// @Order(LOWEST_PRECEDENCE): Garantiza que vaya al final de la lista para evaluarse como último recurso
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class DefaultFeeCalculator implements FeeCalculator {

    // Acepta cualquier tipo de cuenta (actúa como comodín)
    @Override
    public boolean supports(AccountType accountType) {
        return true;
    }

    // Aplica comisión 0 por defecto
    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return BigDecimal.ZERO;
    }
}
