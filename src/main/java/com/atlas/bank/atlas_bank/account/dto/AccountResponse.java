package com.atlas.bank.atlas_bank.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.atlas.bank.atlas_bank.account.model.enums.AccountStatus;
import com.atlas.bank.atlas_bank.account.model.enums.AccountType;

import lombok.Builder;
import lombok.Data;

// DTO de respuesta con los datos públicos de una cuenta bancaria
@Data
@Builder
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private String ownerName;
    private String email;
    private AccountType type;
    private AccountStatus status;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}
