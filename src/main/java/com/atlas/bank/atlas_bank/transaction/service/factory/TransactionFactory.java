package com.atlas.bank.atlas_bank.transaction.service.factory;

import java.math.BigDecimal;
import com.atlas.bank.atlas_bank.transaction.model.Transaction;
import com.atlas.bank.atlas_bank.transaction.model.enums.TransactionStatus;
import com.atlas.bank.atlas_bank.transaction.model.enums.TransactionType;
import com.atlas.bank.atlas_bank.transaction.service.transfer.TransferContext;

/**
 * PATRÓN FACTORY METHOD - Versión Simple (Static Factory)
 * 
 * En Spring Boot normalmente todo es @Component / @Service, pero ESTA CLASE NO.
 * ¿Por qué? Porque es de DOMINIO PURO, no necesita inyectar nada de Spring.
 * Es como una función helper en Node: export function createTransfer(...) {}
 * 
 * OBJETIVO: Que ningún Service haga "new Transaction()" con setters a mano.
 * Si mañana Transaction necesita un campo nuevo (ej. currency), solo tocas
 * aquí.
 */
public class TransactionFactory {

    // Truco para que nadie haga new TransactionFactory(). Solo usamos métodos
    // estáticos.
    private TransactionFactory() {
    }

    /**
     * Crea una transacción de tipo TRANSFER ya lista para guardar.
     * 
     * @param context Es el record que viene del Template Method. Ya trae todo
     *                validado:
     *                - sourceAccount (cuenta origen con su .getId())
     *                - targetAccount (cuenta destino)
     *                - amount (monto)
     * @param fee     Es la comisión calculada en el paso anterior del Template
     *                Method
     * @return Transaction Entidad de dominio lista para que el repository haga
     *         .save()
     */
    public static Transaction createTransfer(TransferContext context, BigDecimal fee) {
        Transaction transaction = new Transaction();

        // Estos 3 son los "strings mágicos" que centralizamos. Ahora son Enums, no
        // Strings.
        transaction.setType(TransactionType.TRANSFER); // ¿Qué tipo es?
        transaction.setStatus(TransactionStatus.EXECUTED); // ¿En qué estado nace?

        // Mapeo del contexto del caso de uso a la entidad
        transaction.setSourceAccountId(context.sourceAccount().getId());
        transaction.setTargetAccountId(context.targetAccount().getId());
        transaction.setAmount(context.amount());
        transaction.setFee(fee); // La fee viene de fuera porque la calcula otro servicio

        return transaction;
    }

    /**
     * Crea una transacción rechazada (REJECTED) para auditoría en base de datos.
     * No aplica comisión (fee = 0) y almacena la razón de bloqueo en la descripción.
     */
    public static Transaction createRejectedTransfer(TransferContext context, String reason) {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.REJECTED);
        transaction.setSourceAccountId(context.sourceAccount().getId());
        transaction.setTargetAccountId(context.targetAccount().getId());
        transaction.setAmount(context.amount());
        transaction.setFee(BigDecimal.ZERO);
        transaction.setDescription(reason);
        return transaction;
    }
}