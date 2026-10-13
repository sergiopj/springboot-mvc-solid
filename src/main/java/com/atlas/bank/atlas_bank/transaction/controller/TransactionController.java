package com.atlas.bank.atlas_bank.transaction.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atlas.bank.atlas_bank.transaction.dto.TransactionMapper;
import com.atlas.bank.atlas_bank.transaction.dto.TransactionResponse;
import com.atlas.bank.atlas_bank.transaction.dto.TransferRequest;
import com.atlas.bank.atlas_bank.transaction.service.ITransactionQueryService;
import com.atlas.bank.atlas_bank.transaction.service.transfer.ITransferService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// Controlador REST para transacciones y transferencias bancarias
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final ITransferService iTransferService;
    private final ITransactionQueryService iTransactionQueryService;
    private final TransactionMapper transactionMapper;

    // Ejecuta una transferencia bancaria entre dos cuentas
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        var saved = iTransferService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionMapper.toResponse(saved));
    }

    // Consulta el histórico de transacciones asociadas a una cuenta
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionList(@PathVariable Long id) {
        var transactions = iTransactionQueryService.getTransactionsByAccountId(id);
        var response = transactions.stream().map(transactionMapper::toResponse).toList();
        return ResponseEntity.ok(response);
    }
}
