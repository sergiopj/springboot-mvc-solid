package com.atlas.bank.atlas_bank.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private String type;
    private String status;
    private BigDecimal balance;
    private LocalDateTime createAt;
}
