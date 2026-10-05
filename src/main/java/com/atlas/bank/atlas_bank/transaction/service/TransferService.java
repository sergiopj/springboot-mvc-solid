package com.atlas.bank.atlas_bank.transaction.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.atlas.bank.atlas_bank.account.exception.AccountNotActiveException;
import com.atlas.bank.atlas_bank.account.exception.AccountNotFoundException;
import com.atlas.bank.atlas_bank.account.model.Account;
import com.atlas.bank.atlas_bank.account.repository.AccountRepository;
import com.atlas.bank.atlas_bank.transaction.dto.TransferRequest;
import com.atlas.bank.atlas_bank.transaction.exception.InsufficientFundsException;
import com.atlas.bank.atlas_bank.transaction.fee.FeeCalculator;
import com.atlas.bank.atlas_bank.transaction.model.Transaction;
import com.atlas.bank.atlas_bank.transaction.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferService implements ITransferService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final List<FeeCalculator> feeCalculators;

    @Override
    @Transactional
    public Transaction execute(TransferRequest request) {
        Long sourceAccountId = request.getSourceAccountId();
        Long targetAccountId = request.getTargetAccountId();
        BigDecimal amount = request.getAmount();

        Account from = accountRepository.findById(sourceAccountId)
                .orElseThrow(() -> new AccountNotFoundException(sourceAccountId));
        Account to = accountRepository.findById(targetAccountId)
                .orElseThrow(() -> new AccountNotFoundException(targetAccountId));

        if (!"ACTIVE".equals(from.getStatus())) {
            throw new AccountNotActiveException(from.getStatus(), sourceAccountId);
        }
        if (!"ACTIVE".equals(to.getStatus())) {
            throw new AccountNotActiveException(to.getStatus(), targetAccountId);
        }

        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(sourceAccountId, amount, from.getBalance());
        }

        BigDecimal fee = feeCalculators.stream()
                .filter(fc -> fc.supports(from.getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay calculador para el tipo " + from.getType()))
                .calculate(amount);

        from.setBalance(from.getBalance().subtract(amount).subtract(fee));
        to.setBalance(to.getBalance().add(amount));
        accountRepository.save(from);
        accountRepository.save(to);

        Transaction transaction = new Transaction();
        transaction.setType("TRANSFER");
        transaction.setSourceAccountId(request.getSourceAccountId());
        transaction.setTargetAccountId(request.getTargetAccountId());
        transaction.setAmount(request.getAmount());
        transaction.setFee(fee);
        transaction.setStatus("EXECUTED");

        return transactionRepository.save(transaction);
    }

}
