package br.com.itau.CasePix.dto;

import br.com.itau.CasePix.model.AccountType;
import br.com.itau.CasePix.model.BankAccountType;
import br.com.itau.CasePix.model.PixKeyType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PixKeyRequestDTO(
        @NotNull PixKeyType keyType,

        @NotBlank
        @Size(max = 77)
        String keyValue,

        @NotNull BankAccountType bankAccountType,

        @NotNull AccountType accountType,

        @NotBlank
        @Size(min = 4, max = 4)
        String agencyNumber,

        @NotBlank
        @Size(min = 8, max = 8)
        String accountNumber,

        @NotBlank
        @Size(max = 30)
        String ownerName,

        @Size(max = 45)
        String ownerLastName
) {}