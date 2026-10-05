package com.atlas.bank.atlas_bank.transaction.dto;

import java.math.BigDecimal;

import com.atlas.bank.atlas_bank.transaction.validation.DifferentAccounts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

// Anotación de Lombok que genera automáticamente en segundo plano:
// getters, setters, toString, equals, hashCode y el constructor para campos requeridos.
@Data
@DifferentAccounts // Anotación custom que valida que sourceAccountId y targetAccountId sean
                   // diferentes
public class TransferRequest {
    // campos que no son string, se recomienda usar @NotNull en lugar de @NotBlank
    // para validar que no sean nulos.
    @NotNull(message = "Source account ID is required")
    private Long sourceAccountId;
    @NotNull(message = "Target account ID is required")
    private Long targetAccountId;
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;
}
