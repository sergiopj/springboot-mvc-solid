package com.atlas.bank.atlas_bank.transaction.service.event;

import java.math.BigDecimal;
import com.atlas.bank.atlas_bank.transaction.model.enums.TransactionType;

/**
 * PATRÓN OBSERVER - El EVENTO (uno de los 3 participantes)
 * 
 * ¿Qué es esto? No es una Entidad, no se guarda en BD. Es un mensaje
 * que dice "¡Oye, acaba de pasar una transacción!".
 * 
 * ¿Por qué es un `record` y no una `class`?
 * 1. Inmutable: Una vez creado no se puede cambiar. Perfecto para un evento.
 * Si fuera una clase con setters, un listener podría modificarlo y liarla.
 * 2. Solo transporta datos: No tiene lógica, solo getters.
 * En Node sería: type TransactionExecutedEvent = { transactionId: string, ... }
 * 
 * En hexagonal este es un DOMAIN EVENT. Vive en Application/Domain.
 * Lo publica el TransferService y lo escuchan los adapters de fuera (email,
 * audit).
 */
public record TransactionExecutedEvent(
        String transactionId, // ID de la transacción que se acaba de guardar
        TransactionType transactionType, // TRANSFER, DEPOSIT... por si un listener solo quiere escuchar un tipo
        Long sourceAccountId, // Quién envía (puede ser null si es un DEPOSIT)
        Long targetAccountId, // Quién recibe (puede ser null si es un WITHDRAWAL)
        BigDecimal amount, // Cuánto dinero se movió
        BigDecimal fee // Cuánta comisión se cobró
) {
    // El record ya te genera constructor, getters, equals y toString automático.
    // No necesitas escribir nada más aquí.
}