package com.atlas.bank.atlas_bank.transaction.service.fee;

import java.math.BigDecimal;

// Interfaz que define el contrato del patrón Strategy para el cálculo de comisiones
public interface FeeCalculator {

    // Indica si esta calculadora aplica para el tipo de cuenta recibido
    boolean supports(String accountType);

    // Ejecuta el cálculo de comisión según el monto
    BigDecimal calculate(BigDecimal amount);
}