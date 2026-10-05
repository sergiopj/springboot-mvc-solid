package com.atlas.bank.atlas_bank.account.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

// DTO (Data Transfer Object) para la creación de cuentas. Contiene los campos necesarios para crear una nueva cuenta bancaria.
// Anotación de Lombok que genera automáticamente en segundo plano:
// getters, setters, toString, equals, hashCode y el constructor para campos requeridos.
@Data
public class CreateAccountRequest {

    // notBlank se usa para validar que los campos de tipo String no sean nulos ni
    // vacíos.
    @NotBlank(message = "Account number is required")
    private String accountNumber;
    @NotBlank(message = "Owner name is required")
    private String ownerName;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Account type is required")
    private String type; // enum SAVING, CHECKING
    @NotNull(message = "Balance is required")
    @PositiveOrZero(message = "Balance must be greater than zero")
    private BigDecimal balance;

}
