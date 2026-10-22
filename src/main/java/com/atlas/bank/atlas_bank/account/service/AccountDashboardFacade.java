package com.atlas.bank.atlas_bank.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.atlas.bank.atlas_bank.account.dto.DashboardResponse;
import com.atlas.bank.atlas_bank.account.model.Account;
import com.atlas.bank.atlas_bank.transaction.dto.TransactionMapper;
import com.atlas.bank.atlas_bank.transaction.dto.TransactionResponse;
import com.atlas.bank.atlas_bank.transaction.service.ITransactionQueryService;

import lombok.AllArgsConstructor;

// Fachada que reúne los datos necesarios para el dashboard y deja esa coordinación
// fuera del controlador. En NestJS, su papel se parece al de un provider de aplicación.
@Service
// Lombok genera un constructor con todos los campos final; Spring lo usa para
// inyectar las dependencias al crear esta clase.
@AllArgsConstructor
public class AccountDashboardFacade {

    // Busca los datos principales de la cuenta.
    private final IAccountService accountService;

    // Obtiene las transacciones asociadas a la cuenta.
    private final ITransactionQueryService transactionQueryService;

    // Convierte cada entidad de transacción al DTO que se devuelve en la API.
    private final TransactionMapper transactionMapper;

    // Coordina las búsquedas y construye la respuesta completa del dashboard.
    public DashboardResponse getDashboard(Long accountId) {

        // Obtiene la cuenta usando el servicio de cuentas.
        Account account = accountService.findById(accountId);

        // Recupera las transacciones y transforma cada una en una respuesta DTO.
        List<TransactionResponse> transactions = transactionQueryService
                .getTransactionsByAccountId(accountId)
                .stream()
                // map aplica toResponse a cada elemento del flujo.
                // La referencia transactionMapper::toResponse equivale a
                // transaction -> transactionMapper.toResponse(transaction).
                .map(transactionMapper::toResponse)
                // Materializa los elementos transformados en una lista.
                .toList();

        // Construye un único DTO con los datos de la cuenta y sus transacciones.
        // builder() permite asignar los campos por nombre y build() crea el objeto final.
        return DashboardResponse.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .ownerName(account.getOwnerName())
                .type(account.getType())
                .status(account.getStatus())
                .balance(account.getBalance())
                .recentTransactions(transactions)
                .build();
    }

}
