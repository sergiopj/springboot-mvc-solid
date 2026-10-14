package com.atlas.bank.atlas_bank.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.atlas.bank.atlas_bank.account.dto.DashboardResponse;
import com.atlas.bank.atlas_bank.account.model.Account;
import com.atlas.bank.atlas_bank.transaction.dto.TransactionMapper;
import com.atlas.bank.atlas_bank.transaction.dto.TransactionResponse;
import com.atlas.bank.atlas_bank.transaction.service.ITransactionQueryService;
import com.atlas.bank.atlas_bank.transaction.service.fraud.FraudCheckResult;
import com.atlas.bank.atlas_bank.transaction.service.fraud.FraudChecker;

import lombok.AllArgsConstructor;

// FINALIDAD quitamos al controller toda esta logica

@Service // inyectable explicar clases etc
@AllArgsConstructor
public class AccountDashboardFacade {

    private final IAccountService accountService;

    private final ITransactionQueryService transactionQueryService;

    private final TransactionMapper transactionMapper;

    public DashboardResponse getDashboard(Long accountId) {

        Account account = accountService.findById(accountId);

        List<TransactionResponse> transactions = transactionQueryService
                .getTransactionsByAccountId(accountId)
                .stream()
                .map(transactionMapper::toResponse) // explicar map
                .toList();

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
