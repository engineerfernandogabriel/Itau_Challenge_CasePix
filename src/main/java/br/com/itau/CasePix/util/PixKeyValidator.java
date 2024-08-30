package br.com.itau.CasePix.util;

import br.com.itau.CasePix.dto.PixKeyRequestDTO;
import br.com.itau.CasePix.model.BankAccountType;
import br.com.itau.CasePix.model.PixKeyType;
import br.com.itau.CasePix.repository.PixKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class PixKeyValidator {

    private static final int MAX_KEYS_PF = 5;
    private static final int MAX_KEYS_PJ = 20;

    private static final Pattern CELULAR_PATTERN = Pattern.compile("^\\+[1-9][0-9]{1,2}[1-9][0-9]{7,10}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern CNPJ_PATTERN = Pattern.compile("^\\d{14}$");
    private static final Pattern CHAVE_ALEATORIA_PATTERN = Pattern.compile("^[a-zA-Z0-9]{36}$");

    private final PixKeyRepository pixKeyRepository;

    public void validatePixKeyRequest(PixKeyRequestDTO request) {
        validateKeyTypeAndValue(request.keyType(), request.keyValue());

        if (pixKeyRepository.existsByKeyValue(request.keyValue())) {
            throw new IllegalArgumentException("Chave Pix já cadastrada");
        }

        validateAgencyNumber(request.agencyNumber());
        validateAccountNumber(request.accountNumber());
        validateOwnerName(request.ownerName(), request.ownerLastName());
    }

    private void validateKeyTypeAndValue(PixKeyType type, String value) {
        switch (type) {
            case CELULAR -> validateCelular(value);
            case EMAIL -> validateEmail(value);
            case CPF -> validateCPF(value);
            case CNPJ -> validateCNPJ(value);
            case ALEATORIO -> validateChaveAleatoria(value);
            default -> throw new IllegalArgumentException("Tipo de Chave Pix Inválido");
        }
    }

    private void validateCelular(String value) {
        if (!CELULAR_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Formato de celular Inválido. O esperado é \"+<DDI><DDD><phone number>\"");
        }
    }

    private void validateEmail(String value) {
        if (!EMAIL_PATTERN.matcher(value).matches() || value.length() > 77) {
            throw new IllegalArgumentException("Email Inválido");
        }
    }

    private void validateCPF(String value) {
        if (!CPF_PATTERN.matcher(value).matches() || !isValidCPF(value)) {
            throw new IllegalArgumentException("CPF Inválido");
        }
    }

    private void validateCNPJ(String value) {
        if (!CNPJ_PATTERN.matcher(value).matches() || !isValidCNPJ(value)) {
            throw new IllegalArgumentException("CNPJ Inválido");
        }
    }

    private void validateChaveAleatoria(String value) {
        if (!CHAVE_ALEATORIA_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Formato de Chave Aleatória Inválido");
        }
    }

    private void validateAgencyNumber(String agencyNumber) {
        if (!agencyNumber.matches("^\\d{4}$")) {
            throw new IllegalArgumentException("Número Agência Inválido");
        }
    }

    private void validateAccountNumber(String accountNumber) {
        if (!accountNumber.matches("^\\d{8}$")) {
            throw new IllegalArgumentException("Número da Conta Inválido");
        }
    }

    private void validateOwnerName(String ownerName, String ownerLastName) {
        if (ownerName.isBlank() || ownerName.length() > 30) {
            throw new IllegalArgumentException("Nome Inválido");
        }
        if (ownerLastName != null && ownerLastName.length() > 45) {
            throw new IllegalArgumentException("Sobrenome Inválido");
        }
    }

    private boolean isValidCPF(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0;
        int weight = 10;
        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * weight--;
        }

        int remainder = (sum * 10) % 11;
        if (remainder == 10) remainder = 0;
        if (remainder != (cpf.charAt(9) - '0')) return false;

        sum = 0;
        weight = 11;
        for (int i = 0; i < 10; i++) {
            sum += (cpf.charAt(i) - '0') * weight--;
        }

        remainder = (sum * 10) % 11;
        if (remainder == 10) remainder = 0;
        return remainder == (cpf.charAt(10) - '0');
    }

    private boolean isValidCNPJ(String cnpj) {
        if (cnpj == null || cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int[] weight1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weight2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (cnpj.charAt(i) - '0') * weight1[i];
        }

        int remainder = (sum % 11);
        int firstDigit = (remainder < 2) ? 0 : (11 - remainder);
        if (firstDigit != (cnpj.charAt(12) - '0')) return false;

        sum = 0;
        for (int i = 0; i < 13; i++) {
            sum += (cnpj.charAt(i) - '0') * weight2[i];
        }

        remainder = (sum % 11);
        int secondDigit = (remainder < 2) ? 0 : (11 - remainder);
        return secondDigit == (cnpj.charAt(13) - '0');
    }

    public void validateKeyLimit(BankAccountType bankAccountTypeType, String accountNumber) {
        long keyCount = pixKeyRepository.countByAccountNumber(accountNumber);
        int maxKeys = (bankAccountTypeType.equals(BankAccountType.PESSOA_JURIDICA)) ? MAX_KEYS_PJ : MAX_KEYS_PF;
        if (keyCount >= maxKeys) {
            throw new IllegalArgumentException("Número máximo de chaves alcançado para esta conta");
        }
    }
}