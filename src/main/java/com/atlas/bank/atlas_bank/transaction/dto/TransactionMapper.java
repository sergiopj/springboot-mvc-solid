package com.atlas.bank.atlas_bank.transaction.dto;

import org.mapstruct.Mapper;

import com.atlas.bank.atlas_bank.transaction.model.Transaction;

// Mapper MapStruct para convertir entidades Transaction a TransactionResponse
@Mapper(componentModel = "spring")
public interface TransactionMapper {

    // Mapea la entidad Transaction a su DTO de respuesta
    TransactionResponse toResponse(Transaction transaction);
}
