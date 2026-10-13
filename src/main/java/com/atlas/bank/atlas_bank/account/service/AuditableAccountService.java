package com.atlas.bank.atlas_bank.account.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import com.atlas.bank.atlas_bank.account.model.Account;
import lombok.extern.slf4j.Slf4j;

/**
 * DECORATOR: No hace trabajo bancario, solo añade auditoría.
 * Es como un envoltorio transparente.
 * 
 * El truco está en 2 beans del mismo tipo IAccountService:
 * 1. AccountService (el real, el que toca la BD) -> se llama "accountService"
 * 2. AuditableAccountService (este, el decorador) -> por @Primary es el que usa
 * todo el mundo
 */
@Component
@Primary // Le dice a Spring: cuando alguien pida IAccountService, dale ESTE, no el otro.
@Slf4j
public class AuditableAccountService implements IAccountService {

    /**
     * DELEGATE = el trabajador real al que le delego el trabajo.
     * Yo solo hago log antes y después, pero el que crea de verdad la cuenta
     * en Postgres es este delegate.
     * 
     * Si no le pones @Qualifier("accountService") Spring intentaría inyectarse
     * a sí mismo (Auditable -> Auditable -> Auditable) y tendrías StackOverflow
     * o dependencia circular.
     * 
     * Con @Qualifier le dices: "inyéctame el bean que se llama accountService,
     * que es la implementación real AccountService, no yo".
     */
    private final IAccountService delegate;

    public AuditableAccountService(@Qualifier("accountService") IAccountService delegate) {
        this.delegate = delegate; // aquí ya tengo al real guardado
    }

    @Override
    public Account create(Account account) {
        // 1. Yo hago mi extra (log de entrada) - no toco lógica bancaria
        log.info("Creating account: {}", account);

        // 2. DELEGO el trabajo real al que sabe: AccountService.create()
        // El flujo va: Controller -> Auditable (yo) -> delegate (AccountService real)
        // -> BD
        Account createdAccount = delegate.create(account);

        // 3. Yo hago mi extra de salida
        log.info("Account created: {}", createdAccount);
        return createdAccount;
    }

    @Override
    public Account findById(Long id) {
        log.info("Finding account by id: {}", id);
        // mismo patrón: yo logueo, el real busca
        Account account = delegate.findById(id);
        log.info("Account found: {}", account);
        return account;
    }

    @Override
    public List<Account> findAll() {
        log.info("Finding all accounts");
        List<Account> accounts = delegate.findAll();
        log.info("Accounts found: {}", accounts);
        return accounts;
    }
}