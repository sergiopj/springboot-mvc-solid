package com.atlas.bank.atlas_bank.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

// DTO de respuesta con el detalle de una transacción procesada
@Data
@Builder
public class TransactionResponse {
    private Long id;
    private String type;
    private Long sourceAccountId;
    private Long targetAccountId;
    private BigDecimal amount;
    private BigDecimal fee;
    private String status;
    private LocalDateTime createdAt;
}
