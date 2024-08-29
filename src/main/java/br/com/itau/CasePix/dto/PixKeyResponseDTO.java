package br.com.itau.CasePix.dto;

import br.com.itau.CasePix.model.AccountType;
import br.com.itau.CasePix.model.BankAccountType;
import br.com.itau.CasePix.model.PixKeyType;

import java.time.LocalDateTime;
import java.util.UUID;

public record PixKeyResponseDTO(
        UUID id,
        PixKeyType keyType,
        String keyValue,
        BankAccountType bankAccountType,
        AccountType accountType,
        String agencyNumber,
        String accountNumber,
        String ownerName,
        String ownerLastName,
        LocalDateTime createdAt,
        LocalDateTime inactivatedAt
) {}
