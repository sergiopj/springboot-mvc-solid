package com.atlas.bank.atlas_bank.transaction.service.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.atlas.bank.atlas_bank.transaction.service.event.TransactionExecutedEvent;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
// En prod: @RequiredArgsConstructor
public class AuditListener {

    // En prod inyectarías: private final AuditPort auditPort;

    @EventListener // Escucha el evento. TransferService no sabe que existes.
    // TODO PROD: @Async + @TransactionalEventListener(AFTER_COMMIT) igual que
    // notificación
    public void onTransactionExecuted(TransactionExecutedEvent event) {
        // DEMO: solo log
        log.info("AUDIT: source={}, target={}, amount={}",
                event.sourceAccountId(), event.targetAccountId(), event.amount());

        // PROD BANCO:
        // auditPort.record(event); -> Guarda en tabla audit_log o lo manda a
        // ELK/CloudWatch
        // Esa tabla es inmutable: nunca se hace UPDATE ni DELETE, solo INSERT.
    }
}