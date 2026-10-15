package com.atlas.bank.atlas_bank.transaction.validation.chain;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.atlas.bank.atlas_bank.transaction.exception.InsufficientFundsException;
import com.atlas.bank.atlas_bank.transaction.service.transfer.TransferContext;

@Component // 1. Otro eslabón que Spring va a meter solo en la List<TransferValidator>
           // Sin tocar TransferService. OCP otra vez.

@Order(2) // 2. ORDEN CLAVE: Va el segundo. 
          // Lógica: primero @Order(1) AccountStatusValidator comprueba que estén ACTIVE.
          // Si no están activas, no tiene sentido ni mirar el saldo. Ahorras una query y evitas NPE.
          // La cadena es un pipeline con lógica de negocio en el orden.

public class SufficientFundsValidator implements TransferValidator {
    @Override
    public void validate(TransferContext context) {
        // 3. SRP EXTREMO: Esta clase hace UNA ÚNICA comparación. Nada más.
        // No sabe qué es una cuenta, no sabe qué es ACTIVE, no sabe qué es fraude.
        // Solo: ¿balance >= amount?
        // Por eso es tan fácil de testear: new Account(balance=100), amount=150 -> debe petar.

        if (context.sourceAccount().getBalance().compareTo(context.amount()) < 0) {
            // 4. Usa compareTo porque es BigDecimal/Money. Nunca == ni > con dinero.
            // Si balance < amount -> insuficiente.
            
            throw new InsufficientFundsException(context.sourceAccount().getId(), context.amount(),
                    context.sourceAccount().getBalance());
            // 5. Si falla, corta la cadena aquí. No llega a @Order(3) FraudValidator.
            // Le pasas datos ricos a la excepción para el ProblemDetail RFC 7807.
        }
    }
    // 6. Fíjate que aquí ya ni siquiera lleva @RequiredArgsConstructor. 
    // No tiene dependencias. Es lógica pura, podría vivir en domain sin Spring si quisieras.
}