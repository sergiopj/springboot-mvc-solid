package com.atlas.bank.atlas_bank.account.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.atlas.bank.atlas_bank.account.model.Account;

// Mapper MapStruct para conversiones entre entidades Account y sus DTOs
@Mapper(componentModel = "spring")
public interface AccountMapper {

    // Mapea la petición a entidad ignorando campos autogenerados por el sistema
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Account toEntity(CreateAccountRequest request);

    // Mapea la entidad Account a su DTO de respuesta
    AccountResponse toResponse(Account account);
}
