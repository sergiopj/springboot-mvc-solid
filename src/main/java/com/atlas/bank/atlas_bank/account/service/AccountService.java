package com.atlas.bank.atlas_bank.account.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.atlas.bank.atlas_bank.account.exception.AccountNotFoundException;
import com.atlas.bank.atlas_bank.account.model.Account;
import com.atlas.bank.atlas_bank.account.model.enums.AccountStatus;
import com.atlas.bank.atlas_bank.account.repository.AccountRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * SERVICIO REAL - Hace el trabajo bancario de verdad.
 * 
 * @Service sin nombre -> Spring lo registra como bean "accountService"
 *          (primera letra en minúscula). Por eso luego el decorador puede hacer
 *          @Qualifier("accountService") para pedir justo a este.
 * 
 *          Este es el delegate. No sabe que existe un decorador por encima.
 *          Solo toca repositorio y reglas de negocio.
 */
@Service // bean name = "accountService"
@RequiredArgsConstructor // Lombok te crea el constructor con el final accountRepository
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository; // puerto de salida

    @Override
    @Transactional // para que Spring abra y cierre la transacción de BD automáticamente
    public Account create(Account account) {
        account.setStatus(AccountStatus.ACTIVE); // invariante de negocio
        return accountRepository.save(account); // guarda bloque Account
    }

    @Override
    @Transactional(readOnly = true) // para que Spring abra y cierre la transacción de BD automáticamente
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true) // para que Spring abra y cierre la transacción de BD automáticamente
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }
}