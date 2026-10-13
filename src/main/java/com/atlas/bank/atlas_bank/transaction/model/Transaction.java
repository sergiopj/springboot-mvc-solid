package com.atlas.bank.atlas_bank.transaction.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.atlas.bank.atlas_bank.transaction.model.enums.TransactionStatus;
import com.atlas.bank.atlas_bank.transaction.model.enums.TransactionType;

// Entidad JPA que representa una transacción financiera
@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20, nullable = false)
    private TransactionType type;

    // ID de la cuenta emisora
    @Column(name = "source_account_id", nullable = false)
    private Long sourceAccountId;

    // ID de la cuenta receptora
    @Column(name = "target_account_id", nullable = false)
    private Long targetAccountId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "fee", nullable = false)
    private BigDecimal fee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private TransactionStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Asigna fecha de creación y estado por defecto antes de persistir
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = TransactionStatus.PENDING;
        }
    }
}