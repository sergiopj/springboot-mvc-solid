package com.atlas.bank.atlas_bank.transaction.service.transfer;

import java.math.BigDecimal;
import org.springframework.transaction.annotation.Transactional;
import com.atlas.bank.atlas_bank.transaction.model.Transaction;
import com.atlas.bank.atlas_bank.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

/**
 * PATRÓN TEMPLATE METHOD - El esqueleto de toda transacción
 * 
 * Vive en: Application Layer (Caso de Uso)
 * 
 * ¿Qué hace? Define los 4 pasos que TODA transacción bancaria debe seguir sí o
 * sí.
 * Si mañana haces Deposit o Withdrawal, no duplicas el flujo, solo implementas
 * los pasos.
 * 
 * En Node/TS sería: abstract class TransactionProcessor<C> donde <C> es el
 * contexto
 * (TransferContext, DepositContext). El genérico evita usar 'any'.
 */
@RequiredArgsConstructor // Lombok: genera el constructor con transactionRepository.
                         // Así Spring lo puede inyectar sin hacer @Autowired a mano.
public abstract class TransactionProcessor<C> {

    // protected: las clases hijas (TransferService) pueden usarlo directamente.
    // final: como 'readonly' en TS, no se puede reasignar. Es un Port hacia la BD.
    protected final TransactionRepository transactionRepository;

    /**
     * MÉTODO PLANTILLA - El orden es innegociable.
     * 
     * @Transactional es lo más importante aquí para un noob:
     *                Equivale a hacer en Prisma: prisma.$transaction(async (tx) =>
     *                { ... })
     *                Si cualquier paso de abajo hace throw, Spring hace ROLLBACK
     *                automático
     *                de todo lo que tocó en la BD (saldos y transacción). Si todo
     *                va bien, hace COMMIT.
     */
    @Transactional
    public Transaction process(C context) {
        validate(context); // 1. ¿Se puede hacer? fondos, cuentas activas
        BigDecimal fee = calculateFee(context); // 2. ¿Cuánto cuesta?
        execute(context, fee); // 3. Mueve el dinero entre cuentas
        return save(context, fee); // 4. Guarda el comprobante
    }

    // Pasos abstractos: cada hijo está OBLIGADO a implementar su propia lógica.
    // Así TransferService valida que origen != destino, y DepositService no.

    protected abstract void validate(C context);

    protected abstract BigDecimal calculateFee(C context);

    protected abstract void execute(C context, BigDecimal fee);

    protected abstract Transaction save(C context, BigDecimal fee);
}