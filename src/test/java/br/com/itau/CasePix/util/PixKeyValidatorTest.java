package br.com.itau.CasePix.util;

import br.com.itau.CasePix.dto.PixKeyRequestDTO;
import br.com.itau.CasePix.model.AccountType;
import br.com.itau.CasePix.model.BankAccountType;
import br.com.itau.CasePix.model.PixKeyType;
import br.com.itau.CasePix.repository.PixKeyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PixKeyValidatorTest {

    @Mock
    private PixKeyRepository pixKeyRepository;

    private PixKeyValidator pixKeyValidator;

    @BeforeEach
    void setUp() {
        pixKeyValidator = new PixKeyValidator(pixKeyRepository);
    }

    @Test
    void testValidatePixKeyRequest_ValidCelular() {
        PixKeyRequestDTO request = createValidPixKeyRequest(PixKeyType.CELULAR, "+5511999999999");
        assertDoesNotThrow(() -> pixKeyValidator.validatePixKeyRequest(request));
    }

    @Test
    void testValidatePixKeyRequest_InvalidCelular() {
        PixKeyRequestDTO request = createValidPixKeyRequest(PixKeyType.CELULAR, "1234567890");
        assertThrows(IllegalArgumentException.class, () -> pixKeyValidator.validatePixKeyRequest(request));
    }

    @Test
    void testValidatePixKeyRequest_ValidEmail() {
        PixKeyRequestDTO request = createValidPixKeyRequest(PixKeyType.EMAIL, "test@example.com");
        assertDoesNotThrow(() -> pixKeyValidator.validatePixKeyRequest(request));
    }

    @Test
    void testValidatePixKeyRequest_InvalidEmail() {
        PixKeyRequestDTO request = createValidPixKeyRequest(PixKeyType.EMAIL, "invalid-email");
        assertThrows(IllegalArgumentException.class, () -> pixKeyValidator.validatePixKeyRequest(request));
    }

    @Test
    void testValidatePixKeyRequest_ValidCPF() {
        PixKeyRequestDTO request = createValidPixKeyRequest(PixKeyType.CPF, "12345678909");
        assertDoesNotThrow(() -> pixKeyValidator.validatePixKeyRequest(request));
    }

    @Test
    void testValidatePixKeyRequest_InvalidCPF() {
        PixKeyRequestDTO request = createValidPixKeyRequest(PixKeyType.CPF, "123456789");
        assertThrows(IllegalArgumentException.class, () -> pixKeyValidator.validatePixKeyRequest(request));
    }

    @Test
    void testValidatePixKeyRequest_ValidCNPJ() {
        PixKeyRequestDTO request = createValidPixKeyPJRequest(PixKeyType.CNPJ, "83293182000163");
        assertDoesNotThrow(() -> pixKeyValidator.validatePixKeyRequest(request));
    }

    @Test
    void testValidatePixKeyRequest_InvalidCNPJ() {
        PixKeyRequestDTO request = createValidPixKeyRequest(PixKeyType.CNPJ, "1234567890");
        assertThrows(IllegalArgumentException.class, () -> pixKeyValidator.validatePixKeyRequest(request));
    }

    @Test
    void testValidatePixKeyRequest_ValidChaveAleatoria() {
        PixKeyRequestDTO request = createValidPixKeyRequest(PixKeyType.ALEATORIO, "7Q9oaYk8GBADlmpUWakPmYB85W1J70PA3A42");
        assertDoesNotThrow(() -> pixKeyValidator.validatePixKeyRequest(request));
    }

    @Test
    void testValidatePixKeyRequest_InvalidChaveAleatoria() {
        PixKeyRequestDTO request = createValidPixKeyRequest(PixKeyType.ALEATORIO, "invalid-random-key");
        assertThrows(IllegalArgumentException.class, () -> pixKeyValidator.validatePixKeyRequest(request));
    }

    @Test
    void testValidatePixKeyRequest_ExistingKey() {
        PixKeyRequestDTO request = createValidPixKeyRequest(PixKeyType.CPF, "12345678909");
        when(pixKeyRepository.existsByKeyValue("12345678909")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> pixKeyValidator.validatePixKeyRequest(request));
    }

    @Test
    void testValidateKeyLimit_WithinLimit() {
        when(pixKeyRepository.countByAccountNumber("12345678")).thenReturn(3L);
        assertDoesNotThrow(() -> pixKeyValidator.validateKeyLimit(BankAccountType.PESSOA_FISICA, "12345678"));
    }

    @Test
    void testValidateKeyLimit_ExceedsLimit() {
        when(pixKeyRepository.countByAccountNumber("12345678")).thenReturn(5L);
        assertThrows(IllegalArgumentException.class,
                () -> pixKeyValidator.validateKeyLimit(BankAccountType.PESSOA_FISICA, "12345678"));
    }

    private PixKeyRequestDTO createValidPixKeyRequest(PixKeyType keyType, String keyValue) {
        return new PixKeyRequestDTO(
                keyType,
                keyValue,
                BankAccountType.PESSOA_FISICA,
                AccountType.CORRENTE,
                "1234",
                "12345678",
                "Bruce",
                "Wayne"
        );
    }

    private PixKeyRequestDTO createValidPixKeyPJRequest(PixKeyType keyType, String keyValue) {
        return new PixKeyRequestDTO(
                keyType,
                keyValue,
                BankAccountType.PESSOA_JURIDICA,
                AccountType.CORRENTE,
                "1234",
                "12345678",
                "Peter",
                "Parker"
        );
    }
}