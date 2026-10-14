package com.atlas.bank.atlas_bank.transaction.service.fraud;

/**
 * Resultado de dominio para verificación de fraude.
 * Es lo que entiende nuestro dominio, no la API externa.
 * La API externa devuelve riskLevel, score, recommendation...
 * El Adapter lo traduce a esto: ¿bloqueado? ¿por qué?
 */
public record FraudCheckResult(
        boolean blocked, // true = operación bloqueada por fraude
        String reason // motivo del bloqueo, null si está permitida
) {

    /**
     * Factory para caso permitido.
     * Uso: FraudCheckResult.allowed()
     */
    public static FraudCheckResult allowed() {
        return new FraudCheckResult(false, null);
    }

    /**
     * Factory para caso bloqueado.
     * 
     * @param reason por qué se bloquea (ej: "score alto", "país en lista negra")
     */
    public static FraudCheckResult blocked(String reason) {
        return new FraudCheckResult(true, reason);
    }
}