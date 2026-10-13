package com.atlas.bank.atlas_bank.transaction.service.transfer;

import java.math.BigDecimal;
import com.atlas.bank.atlas_bank.account.model.Account;

/**
 * PATRÓN TEMPLATE METHOD - El OBJETO DE CONTEXTO
 * 
 * ¿Qué es esto? Un DTO interno de la capa de Application. No es el Request del
 * Controller.
 * 
 * Diferencia clave para noobs:
 * - TransferRequest (lo que entra por el controller): solo trae IDs -> {
 * sourceId: 1, targetId: 2, amount: 100 }
 * - TransferContext (esto): ya trae las ENTIDADES completas de BD listas para
 * operar.
 * Se crea después de hacer accountRepository.findById().
 * 
 * ¿Por qué es un `record`?
 * 1. Inmutable: como poner `readonly` en TS o `Object.freeze()`. Una vez creado
 * no cambia.
 * 2. Agrupa parámetros: En vez de pasar 3 parámetros sueltos por todos los
 * métodos
 * del Template (validate, calculateFee, execute, save), pasas un solo `ctx`.
 * Es el mismo `ctx` de Express / NestJS.
 * 
 * En Node sería:
 * interface TransferContext { readonly sourceAccount: Account; readonly
 * targetAccount: Account; readonly amount: BigDecimal; }
 */
public record TransferContext(
        Account sourceAccount, // Entidad completa de la cuenta origen (ya validada que existe)
        Account targetAccount, // Entidad completa de la cuenta destino
        BigDecimal amount // Monto a transferir, ya validado > 0
) {
    // El record genera solo el constructor y los getters: sourceAccount(),
    // targetAccount(), amount()
}