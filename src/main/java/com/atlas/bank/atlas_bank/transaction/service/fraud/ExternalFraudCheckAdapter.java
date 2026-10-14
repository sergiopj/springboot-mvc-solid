package com.atlas.bank.atlas_bank.transaction.service.fraud;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import com.atlas.bank.atlas_bank.transaction.service.fraud.enums.Recommendation;
import com.atlas.bank.atlas_bank.transaction.service.fraud.enums.RiskLevel;
import lombok.extern.slf4j.Slf4j;

/**
 * ADAPTER - En NestJS sería tu @Injectable()
 * 
 * ¿Qué hace?
 * Implementa FraudChecker (el contrato) pero por dentro habla con el mundo
 * externo.
 * Traduce ExternalFraudResponse (riesgo, score) -> FraudCheckResult
 * (blocked/allowed)
 * 
 * MVC: sería como tu Service de Nest que llama a una API externa.
 */
@Component // En NestJS = @Injectable(). Le dices a Spring: "créame, gestiónalo tú".
           // Singleton.
@Slf4j // Lombok te crea un Logger = private static final Logger log. Como tu Logger de
       // Nest.
public class ExternalFraudCheckAdapter implements FraudChecker {

    /**
     * @Override = estoy cumpliendo el contrato de la interfaz FraudChecker
     *           En NestJS es cuando haces implements y tienes que escribir el
     *           método check().
     *           Si no lo pones, no compila.
     */
    @Override
    public FraudCheckResult check(Long accountId, BigDecimal amount) {

        // SIMULACIÓN de llamada a tu otro contenedor de IA
        // En prod sería: restTemplate.post("http://fraud-ai:8000/check", ...)
        ExternalFraudResponse response = callExternalFraudApi(accountId, amount);

        // OJO: tu log estaba mal, faltan los {} para interpolar
        // Antes: log.info("... Response", level, score, rec) -> no pinta nada
        // Ahora sí:
        log.info("External fraud api - risk={} score={} recommendation={}",
                response.getRiskLevel(), response.getScore(), response.getRecommendation());

        // Traductor: si la IA externa dice REJECT, nosotros devolvemos blocked() de
        // DOMINIO
        // El dominio nunca ve RiskLevel ni score
        if (Recommendation.REJECT.equals(response.getRecommendation())) {
            return FraudCheckResult.blocked(
                    String.format("Operation rejected due to high risk (score: %.2f)", response.getScore()));
        }

        // Si no es bloqueado, todo ok
        return FraudCheckResult.allowed();
    }

    /**
     * FAKE - Mock del contenedor de IA
     * No pegamos a API real aún. Simulamos reglas de negocio.
     * En NestJS sería tu método privado que hace fetch().
     */
    private ExternalFraudResponse callExternalFraudApi(Long accountId, BigDecimal amount) {
        // Regla fake: si amount > 10.000.000 -> sospechoso
        if (amount.compareTo(new BigDecimal(10000000)) > 0) {
            return new ExternalFraudResponse(RiskLevel.HIGH, 0.95, Recommendation.REJECT);
        }
        // Si no, riesgo bajo
        return new ExternalFraudResponse(RiskLevel.LOW, 0.1, Recommendation.APPROVE);
    }
}