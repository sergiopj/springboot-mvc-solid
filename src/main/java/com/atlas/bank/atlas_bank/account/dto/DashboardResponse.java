package com.atlas.bank.atlas_bank.account.dto;

import java.math.BigDecimal;
import java.util.List;
import com.atlas.bank.atlas_bank.account.model.enums.AccountStatus;
import com.atlas.bank.atlas_bank.account.model.enums.AccountType;
import com.atlas.bank.atlas_bank.transaction.dto.TransactionResponse;
import lombok.Builder;
import lombok.Data;

/**
 * FACADE - Producto final.
 * Es el DTO unificado que devuelve facade.getDashboard(id).
 * 
 * ¿De dónde sale cada campo?
 * - accountId, accountNumber, ownerName, type, status, balance ->
 * AccountService
 * - recentTransactions -> TransactionQuery
 * - fraudBlocked, fraudReason -> FraudChecker (tu Adapter de IA)
 * 
 * Sin Facade, esto lo armaba el Controller a mano. Con Facade, lo arma el
 * Facade.
 */
@Data // Lombok: te genera @Getter, @Setter, @ToString, @EqualsAndHashCode,
      // @RequiredArgsConstructor
      // En NestJS es como si te hiciera los get/set automáticos, sin escribirlos.
@Builder // Patrón Builder: te deja construir el objeto paso a paso
         // En NestJS sería como new
         // DashboardResponseBuilder().setAccount().setTxns().build()
public class DashboardResponse {

    // Datos de cuenta - vienen de AccountService
    private Long accountId;
    private String accountNumber;
    private String ownerName;
    private AccountType type;
    private AccountStatus status;
    private BigDecimal balance;

    // Transacciones - vienen de TransactionQuery
    private List<TransactionResponse> recentTransactions;

}