package com.atlas.bank.atlas_bank.transaction.dto;

import java.math.BigDecimal;

import com.atlas.bank.atlas_bank.transaction.validation.DifferentAccounts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

// DTO para solicitar una transferencia de fondos entre dos cuentas
@Data
@DifferentAccounts
public class TransferRequest {

    @NotNull(message = "Source account ID is required")
    private Long sourceAccountId;

    @NotNull(message = "Target account ID is required")
    private Long targetAccountId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;
}
