package com.atlas.bank.atlas_bank.transaction.service.transfer;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.atlas.bank.atlas_bank.account.exception.AccountNotActiveException;
import com.atlas.bank.atlas_bank.account.exception.AccountNotFoundException;
import com.atlas.bank.atlas_bank.account.model.Account;
import com.atlas.bank.atlas_bank.account.model.enums.AccountStatus;
import com.atlas.bank.atlas_bank.account.repository.AccountRepository;
import com.atlas.bank.atlas_bank.transaction.dto.TransferRequest;
import com.atlas.bank.atlas_bank.transaction.exception.InsufficientFundsException;
import com.atlas.bank.atlas_bank.transaction.model.Transaction;
import com.atlas.bank.atlas_bank.transaction.repository.TransactionRepository;
import com.atlas.bank.atlas_bank.transaction.service.event.TransactionExecutedEvent;
import com.atlas.bank.atlas_bank.transaction.service.factory.TransactionFactory;
import com.atlas.bank.atlas_bank.transaction.service.fee.FeeCalculator;

// Servicio de negocio para la ejecución de transferencias entre cuentas
@Service
public class TransferService extends TransactionProcessor<TransferContext> implements ITransferService {

    private final AccountRepository accountRepository;
    private final List<FeeCalculator> feeCalculators;
    private final ApplicationEventPublisher eventPublisher;

    // Inyecta las dependencias propias y pasa el repositorio a la clase padre
    // mediante super(...) CONSTRUCTOR
    public TransferService(TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            List<FeeCalculator> feeCalculators,
            ApplicationEventPublisher eventPublisher) {
        super(transactionRepository);
        this.accountRepository = accountRepository;
        this.feeCalculators = feeCalculators;
        this.eventPublisher = eventPublisher;
    }

    // Punto de entrada: busca las cuentas en BD, arma el contexto y lanza el
    // Template Method
    @Override
    public Transaction execute(TransferRequest request) {
        Account from = accountRepository.findById(request.getSourceAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getSourceAccountId()));

        Account to = accountRepository.findById(request.getTargetAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getTargetAccountId()));
        TransferContext context = new TransferContext(from, to, request.getAmount());

        // 1. Ejecuta el Template Method (valida, aplica comisiones, transfiere y guarda en BD)
        Transaction transaction = process(context);

        // 2. Publica el evento con los datos reales de la transacción persistida
        eventPublisher.publishEvent(new TransactionExecutedEvent(
                String.valueOf(transaction.getId()),
                transaction.getType(),
                transaction.getSourceAccountId(),
                transaction.getTargetAccountId(),
                transaction.getAmount(),
                transaction.getFee()
        ));

        return transaction;
    }

    // Paso 1: Valida que ambas cuentas estén activas y que haya saldo suficiente
    @Override
    protected void validate(TransferContext context) {
        if (context.sourceAccount().getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(context.sourceAccount().getStatus().name(),
                    context.sourceAccount().getId());
        }

        if (context.targetAccount().getStatus() != AccountStatus.ACTIVE) {
            throw new AccountNotActiveException(context.targetAccount().getStatus().name(),
                    context.targetAccount().getId());
        }

        if (context.sourceAccount().getBalance().compareTo(context.amount()) < 0) {
            throw new InsufficientFundsException(context.sourceAccount().getId(), context.amount(),
                    context.sourceAccount().getBalance());
        }
    }

    // Paso 2: Selecciona la estrategia de comisión adecuada y calcula el monto
    @Override
    protected BigDecimal calculateFee(TransferContext context) {
        FeeCalculator feeCalculator = feeCalculators.stream()
                .filter(fc -> fc.supports(context.sourceAccount().getType().name()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "No hay calculador para el tipo " + context.sourceAccount().getType()));
        return feeCalculator.calculate(context.amount());
    }

    // Paso 3: Aplica los débitos y créditos en los saldos de ambas cuentas
    @Override
    protected void execute(TransferContext context, BigDecimal fee) {
        Account from = context.sourceAccount();
        Account to = context.targetAccount();
        BigDecimal amount = context.amount();

        from.setBalance(from.getBalance().subtract(amount).subtract(fee));
        to.setBalance(to.getBalance().add(amount));

        accountRepository.save(from);
        accountRepository.save(to);
    }

    // Paso 4: Construye y guarda el registro de la transacción ejecutada en la BD
    @Override
    protected Transaction save(TransferContext context, BigDecimal fee) {
        Transaction transaction = transactionRepository.save(TransactionFactory.createTransfer(context, fee));
        return transaction;
    }
}