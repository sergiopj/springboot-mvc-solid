package com.atlas.bank.atlas_bank.transaction.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.atlas.bank.atlas_bank.transaction.model.Transaction;
import com.atlas.bank.atlas_bank.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

@Service // 1. Es un servicio de APLICACIÓN, pero solo de lectura.
         // No es @Component genérico como los validadores, es @Service porque es caso de uso.
@RequiredArgsConstructor // 2. Inyección por constructor. Solo depende de una cosa: el repo.

public class TransactionQueryService implements ITransactionQueryService {
    // 3. IMPLEMENTA INTERFAZ: ITransactionQueryService. 
    // Esto te permite en el controller depender de la interfaz (puerto) y no de la clase.
    // Ya es el primer paso hacia hexagonal.

    private final TransactionRepository transactionRepository;
    // 4. ÚNICA DEPENDENCIA: Solo el repository. No tiene validadores, no tiene otros services,
    // no tiene lógica de negocio. Si solo depende de una cosa, solo tiene una razón para cambiar.

    @Override
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        // 5. SRP PURO: Esta clase hace UNA SOLA COSA: traer transacciones de una cuenta.
        // No crea transacciones, no valida estado, no comprueba fondos, no lanza eventos.
        // Para eso ya tienes TransferService (el Command).
        
        return transactionRepository
                .findBySourceAccountIdOrTargetAccountId(accountId, accountId);
        // 6. Delega 100% al repo. No hay ifs, no hay lógica.
        // Esto es el Query de CQRS. Lectura optimizada y tonta.
        // Mañana, si quieres devolver un DTO con saldo, o paginar, o usar una vista SQL,
        // solo tocas esta clase. TransferService ni se entera.
    }
}