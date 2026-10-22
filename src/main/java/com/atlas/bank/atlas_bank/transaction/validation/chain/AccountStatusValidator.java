package com.atlas.bank.atlas_bank.transaction.validation.chain;
// 1. Paquete dedicado. Ya no está todo metido en service, está aislado.

//    Cada regla vive en su propia clase.

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.atlas.bank.atlas_bank.account.exception.AccountNotActiveException;
import com.atlas.bank.atlas_bank.account.model.enums.AccountStatus;
import com.atlas.bank.atlas_bank.transaction.service.transfer.TransferContext;
import lombok.RequiredArgsConstructor;

@Component // 2. MAGIA DE SPRING: al poner @Component, Spring lo detecta solo
           // y lo mete en la List<TransferValidator> que inyecta TransferService.
           // Para añadir una regla nueva, solo creas otra clase con @Component.
           // Cero cambios en TransferService -> Open/Closed Principle.

@Order(1) // 3. ORDEN DE LA CADENA: Este es el eslabón 1. Es el primero que se ejecuta.
          // ¿Por qué? Porque no tiene sentido comprobar si hay fondos
          // si la cuenta ni siquiera está activa. El orden importa.

@RequiredArgsConstructor // 4. Lombok. Aquí no tiene fields final ahora, pero te deja
                         // preparado para inyectar un repository si mañana lo necesitas,
                         // sin tener que tocar el constructor.

public class AccountStatusValidator implements TransferValidator {
    // 5. IMPLEMENTA LA INTERFAZ COMÚN: Todos los validadores hablan el mismo
    // idioma.
    // Esto es lo que permite hacer validators.forEach(v -> v.validate(ctx))

    @Override
    public void validate(TransferContext context) {
        // 6. SINGLE RESPONSIBILITY PRINCIPLE - Aquí está la clave:
        // Esta clase hace UNA SOLA COSA: comprobar que las cuentas están ACTIVE.
        // No comprueba fondos, no comprueba fraude, no comprueba límite diario.
        // Antes, TransferService tenía 5 responsabilidades. Ahora cada una está
        // separada.

        if (context.sourceAccount().getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(context.sourceAccount().getStatus(),
                    context.sourceAccount().getId());
        }
        // Si la origen no está activa, LANZA EXCEPCIÓN y la cadena se corta aquí.
        // No se ejecutan ni SufficientFundsValidator ni FraudValidator.

        if (context.targetAccount().getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(context.targetAccount().getStatus(),
                    context.targetAccount().getId());
        }
        // Misma idea para destino. Aunque haya 2 ifs, sigue siendo la MISMA
        // responsabilidad:
        // "estado de cuentas". No está mezclando dominios.
    }
}