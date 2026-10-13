package com.atlas.bank.atlas_bank.account.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

// DTO para la solicitud de creación de una cuenta bancaria con validaciones
@Data
public class CreateAccountRequest {

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "Owner name is required")
    private String ownerName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Account type is required")
    private String type;

    @NotNull(message = "Balance is required")
    @PositiveOrZero(message = "Balance must be greater than zero")
    private BigDecimal balance;
}
