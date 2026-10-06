package com.atlas.bank.atlas_bank.account.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.atlas.bank.atlas_bank.account.model.enums.AccountStatus;
import com.atlas.bank.atlas_bank.account.model.enums.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@Getter
@Setter
// Genera un constructor vacío (sin parámetros).
// Es obligatorio para JPA/Hibernate, ya que lo necesita para instanciar el
// objeto al hacer consultas a la BD.
@NoArgsConstructor
// Genera un constructor con todos los atributos de la clase como parámetros.
// Permite instanciar objetos completos fácilmente (muy útil en tests y mapeos).
@AllArgsConstructor
// Solo incluye en equals() y hashCode() los campos marcados explícitamente con
// @EqualsAndHashCode.Include
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental generado automáticamente por la BD
    @EqualsAndHashCode.Include // Dos cuentas se consideran la misma si comparten el mismo ID
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "email", nullable = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20, nullable = false)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private AccountStatus status;

    @Column(name = "balance", length = 20, nullable = false)
    private BigDecimal balance;

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    /**
     * Callback del ciclo de vida de JPA.
     * Se ejecuta automáticamente justo antes del primer INSERT (@PrePersist).
     * Garantiza valores por defecto para evitar nulos y estados inconsistentes
     * si el cliente no los proporciona al crear la cuenta.
     */
    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
        if (status == null) {
            status = AccountStatus.ACTIVE;
        }
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
    }

}
