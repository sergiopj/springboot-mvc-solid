package com.atlas.bank.atlas_bank.transaction.service.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.atlas.bank.atlas_bank.transaction.service.event.TransactionExecutedEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * PATRÓN OBSERVER - LISTENER (versión demo)
 * Vive en: Infrastructure Layer
 * 
 * Este listener NO envía emails/SMS/Push él mismo. Solo es el que REACCIONA al
 * evento
 * y DELEGA el trabajo a un servicio/port especializado.
 */
@Component
@Slf4j
// En producción llevaría @RequiredArgsConstructor para inyectar los puertos
public class NotificationListener {

    // En producción real inyectarías los puertos/servicios aquí:
    // private final EmailPort emailPort; // -> EmailNotificationAdapter
    // (JavaMailSender)
    // private final SmsPort smsPort; // -> SmsNotificationAdapter (Twilio)
    // private final PushPort pushPort; // -> PushNotificationAdapter (Firebase con
    // el serviceAccountKey.json de Nest)

    @EventListener // escucha el evento TransactionExecutedEvent y ejecuta este método cuando se
                   // publica
    public void onTransactionExecuted(TransactionExecutedEvent event) {
        // DEMO: de momento solo log para no configurar SMTP/Firebase
        log.info("Notification sending: Transaction executed: {}", event);

        // PRODUCCIÓN: aquí llamarías a los servicios. El listener solo orquesta:
        // emailPort.sendEmail(event.sourceAccountId(), event.amount());
        // smsPort.sendSms(event.sourceAccountId(), "Te han transferido " +
        // event.amount());
        // pushPort.sendPush(event.targetAccountId(), event);

        // NOTA HEXAGONAL: EmailPort/SmsPort/PushPort son interfaces en Application.
        // Sus implementaciones reales (JavaMailSender, Twilio, Firebase) viven en
        // Infrastructure.
        // Si mañana cambias Firebase por otro, solo cambias el adapter, este listener
        // no se toca.
    }
}