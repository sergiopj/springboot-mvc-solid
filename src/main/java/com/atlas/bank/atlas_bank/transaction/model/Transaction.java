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

// Entidad JPA vinculada a la base de datos.
@Entity
@Table(name = "transactions")
// Lombok: genera automáticamente los métodos getter y setter de todos los
// atributos.
@Getter
@Setter
// Genera un constructor vacío (sin parámetros).
// Es obligatorio para JPA/Hibernate, ya que lo necesita para instanciar el
// objeto al hacer consultas a la BD.
@NoArgsConstructor
// Genera un constructor con todos los atributos de la clase como parámetros.
// Permite instanciar objetos completos fácilmente (muy útil en tests y mapeos).
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20, nullable = false)
    private TransactionType type;

    // ID de la cuenta origen (de donde sale el dinero).
    // Se utiliza el ID numérico (Long) en lugar del número de cuenta (String) porque
    // las búsquedas e índices por clave técnica numérica son mucho más rápidos en la BD.
    @Column(name = "source_account_id", nullable = false)
    private Long sourceAccountId;

    // ID de la cuenta destino (a donde ingresa el dinero).
    // Vincula de forma inmutable la transacción con la cuenta beneficiaria.
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

    /**
     * Callback del ciclo de vida de JPA.
     * Se ejecuta automáticamente justo antes del primer INSERT (@PrePersist).
     * Garantiza valores por defecto para evitar nulos y estados inconsistentes
     * si el cliente no los proporciona al crear la cuenta.
     */

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null)
            this.status = TransactionStatus.PENDING;
    }
}