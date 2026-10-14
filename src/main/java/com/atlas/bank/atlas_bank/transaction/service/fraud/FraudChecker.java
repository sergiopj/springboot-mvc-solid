package com.atlas.bank.atlas_bank.transaction.service.fraud;

import java.math.BigDecimal;

/**
 * PORT - Contrato que define QUÉ necesitamos, no CÓMO se hace.
 * En Node sería como tu interface FraudChecker { check(): Promise<Result> }
 * 
 * ¿Para qué sirve?
 * TransferService no quiere saber si el fraude lo comprueba una IA,
 * un if cutre o una API externa. Solo quiere preguntar:
 * "¿Esta cuenta puede mover esta cantidad?"
 * 
 * Así desacoplamos. Si mañana cambias de vendor, TransferService no se toca.
 */
public interface FraudChecker {

    /**
     * Comprueba si una operación es sospechosa.
     * 
     * @param accountId cuenta origen
     * @param amount    importe a mover
     * @return FraudCheckResult -> allowed() o blocked("motivo")
     */
    FraudCheckResult check(Long accountId, BigDecimal amount);
}