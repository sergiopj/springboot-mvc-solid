package com.atlas.bank.atlas_bank.transaction.service.fraud;

import com.atlas.bank.atlas_bank.transaction.service.fraud.enums.Recommendation;
import com.atlas.bank.atlas_bank.transaction.service.fraud.enums.RiskLevel;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO que devuelve el servicio externo de fraude (tu contenedor de IA).
 * Es el mundo externo: riskLevel, score, recommendation.
 * No debe llegar a TransferService. El Adapter lo traduce a FraudCheckResult.
 */
@Getter
@AllArgsConstructor
public class ExternalFraudResponse {

    /** Nivel de riesgo que calcula la IA: LOW, MEDIUM, HIGH */
    private RiskLevel riskLevel;

    /** Score de fraude de 0.0 a 1.0. Ej: 0.95 = muy sospechoso */
    private Double score; // 0.0 a 1.0

    /** Recomendación de la IA: ALLOW, REVIEW, BLOCK */
    private Recommendation recommendation;
}