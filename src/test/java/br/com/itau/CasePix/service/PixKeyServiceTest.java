package br.com.itau.CasePix.service;

import br.com.itau.CasePix.dto.PixKeyRequestDTO;
import br.com.itau.CasePix.dto.PixKeyResponseDTO;
import br.com.itau.CasePix.model.AccountType;
import br.com.itau.CasePix.model.BankAccountType;
import br.com.itau.CasePix.model.PixKey;
import br.com.itau.CasePix.model.PixKeyType;
import br.com.itau.CasePix.repository.PixKeyRepository;
import br.com.itau.CasePix.util.PixKeyValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PixKeyServiceTest {

    @Mock
    private PixKeyRepository pixKeyRepository;

    @Mock
    private PixKeyValidator pixKeyValidator;

    @InjectMocks
    private PixKeyService pixKeyService;

    private PixKeyRequestDTO validRequest;
    private PixKey validPixKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validRequest = new PixKeyRequestDTO(
                PixKeyType.CPF,
                "12345678901",
                BankAccountType.PESSOA_FISICA,
                AccountType.CORRENTE,
                "1234",
                "12345678",
                "Peter",
                "Parker"
        );

        validPixKey = new PixKey();
        validPixKey.setId(UUID.randomUUID());
        validPixKey.setKeyType(PixKeyType.CPF);
        validPixKey.setKeyValue("12345678901");
        validPixKey.setBankAccountType(BankAccountType.PESSOA_FISICA);
        validPixKey.setAccountType(AccountType.CORRENTE);
        validPixKey.setAgencyNumber("1234");
        validPixKey.setAccountNumber("12345678");
        validPixKey.setOwnerName("Peter");
        validPixKey.setOwnerLastName("Parker");
        validPixKey.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createPixKey_Success() {
        when(pixKeyRepository.save(any(PixKey.class))).thenReturn(validPixKey);

        PixKeyResponseDTO response = pixKeyService.createPixKey(validRequest);

        assertNotNull(response);
        assertEquals(validPixKey.getId(), response.id());
        assertEquals(validPixKey.getKeyType(), response.keyType());
        assertEquals(validPixKey.getKeyValue(), response.keyValue());

        verify(pixKeyValidator).validateKeyLimit(validRequest.bankAccountType(), validRequest.accountNumber());
        verify(pixKeyValidator).validatePixKeyRequest(validRequest);
        verify(pixKeyRepository).save(any(PixKey.class));
    }

    @Test
    void updatePixKey_Success() {
        UUID id = UUID.randomUUID();
        PixKey existingPixKey = new PixKey();
        existingPixKey.setId(id);
        existingPixKey.setKeyType(PixKeyType.CPF);
        existingPixKey.setKeyValue("98765432100");  // Diferente do validRequest
        existingPixKey.setBankAccountType(BankAccountType.PESSOA_FISICA);
        existingPixKey.setAccountType(AccountType.CORRENTE);
        existingPixKey.setAgencyNumber("1234");
        existingPixKey.setAccountNumber("12345678");
        existingPixKey.setOwnerName("Bruce");
        existingPixKey.setOwnerLastName("Wayne");
        existingPixKey.setCreatedAt(LocalDateTime.now());

        when(pixKeyRepository.findById(id)).thenReturn(Optional.of(existingPixKey));
        when(pixKeyRepository.save(any(PixKey.class))).thenReturn(existingPixKey);

        PixKeyResponseDTO response = pixKeyService.updatePixKey(id, validRequest);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals(validRequest.keyType(), response.keyType());
        assertEquals(existingPixKey.getKeyValue(), response.keyValue());  // O valor da chave não deve mudar
        assertEquals(validRequest.ownerName(), response.ownerName());

        verify(pixKeyRepository).findById(id);
        verify(pixKeyValidator).validatePixKeyRequest(validRequest);
        verify(pixKeyRepository).save(any(PixKey.class));
    }

    @Test
    void updatePixKey_NonExistentId_ThrowsEntityNotFoundException() {
        UUID id = UUID.randomUUID();
        when(pixKeyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> pixKeyService.updatePixKey(id, validRequest));
    }

    @Test
    void updatePixKey_InactivatedKey_ThrowsIllegalStateException() {
        UUID id = UUID.randomUUID();
        PixKey inactivatedPixKey = new PixKey();
        inactivatedPixKey.setId(id);
        inactivatedPixKey.setInactivatedAt(LocalDateTime.now());

        when(pixKeyRepository.findById(id)).thenReturn(Optional.of(inactivatedPixKey));

        assertThrows(IllegalStateException.class, () -> pixKeyService.updatePixKey(id, validRequest));
    }

    @Test
    void inactivatePixKey_Success() {
        UUID id = UUID.randomUUID();
        when(pixKeyRepository.findById(id)).thenReturn(Optional.of(validPixKey));
        when(pixKeyRepository.save(any(PixKey.class))).thenReturn(validPixKey);

        PixKeyResponseDTO response = pixKeyService.inactivatePixKey(id);

        assertNotNull(response);
        assertNotNull(response.inactivatedAt());

        verify(pixKeyRepository).findById(id);
        verify(pixKeyRepository).save(any(PixKey.class));
    }

    @Test
    void inactivatePixKey_NonExistentId_ThrowsEntityNotFoundException() {
        UUID id = UUID.randomUUID();
        when(pixKeyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> pixKeyService.inactivatePixKey(id));
    }

    @Test
    void inactivatePixKey_AlreadyInactive_ThrowsIllegalStateException() {
        UUID id = UUID.randomUUID();
        PixKey inactivePixKey = new PixKey();
        inactivePixKey.setInactivatedAt(LocalDateTime.now());

        when(pixKeyRepository.findById(id)).thenReturn(Optional.of(inactivePixKey));

        assertThrows(IllegalStateException.class, () -> pixKeyService.inactivatePixKey(id));
    }

    @Test
    void getPixKeyById_Success() {
        when(pixKeyRepository.findById(validPixKey.getId())).thenReturn(Optional.of(validPixKey));

        PixKeyResponseDTO response = pixKeyService.getPixKeyById(validPixKey.getId());

        assertNotNull(response);
        assertEquals(validPixKey.getId(), response.id());
    }

    @Test
    void getPixKeyById_NonExistentId_ThrowsEntityNotFoundException() {
        UUID id = UUID.randomUUID();
        when(pixKeyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> pixKeyService.getPixKeyById(id));
    }

    @Test
    void getPixKeysByType_Success() {
        List<PixKey> pixKeys = Arrays.asList(validPixKey);
        when(pixKeyRepository.findByKeyType(PixKeyType.CPF)).thenReturn(pixKeys);

        List<PixKeyResponseDTO> response = pixKeyService.getPixKeysByType(PixKeyType.CPF);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(validPixKey.getId(), response.get(0).id());
    }

    @Test
    void getPixKeysByAgencyAndAccount_Success() {
        List<PixKey> pixKeys = Arrays.asList(validPixKey);
        when(pixKeyRepository.findByAgencyNumberAndAccountNumber("1234", "12345678")).thenReturn(pixKeys);

        List<PixKeyResponseDTO> response = pixKeyService.getPixKeysByAgencyAndAccount("1234", "12345678");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(validPixKey.getId(), response.get(0).id());
    }

    @Test
    void getPixKeysByOwnerName_Success() {
        List<PixKey> pixKeys = Arrays.asList(validPixKey);
        when(pixKeyRepository.findByOwnerNameContainingIgnoreCase("John")).thenReturn(pixKeys);

        List<PixKeyResponseDTO> response = pixKeyService.getPixKeysByOwnerName("John");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(validPixKey.getId(), response.get(0).id());
    }

    @Test
    void getPixKeysByCreationDateRange_Success() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<PixKey> pixKeys = Arrays.asList(validPixKey);
        when(pixKeyRepository.findByCreatedAtBetween(start, end)).thenReturn(pixKeys);

        List<PixKeyResponseDTO> response = pixKeyService.getPixKeysByCreationDateRange(start, end);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(validPixKey.getId(), response.get(0).id());
    }

    @Test
    void getPixKeysByInactivationDateRange_Success() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        PixKey inactivePixKey = new PixKey();
        inactivePixKey.setId(UUID.randomUUID());
        inactivePixKey.setInactivatedAt(LocalDateTime.now());
        List<PixKey> pixKeys = Arrays.asList(inactivePixKey);
        when(pixKeyRepository.findByInactivatedAtBetween(start, end)).thenReturn(pixKeys);

        List<PixKeyResponseDTO> response = pixKeyService.getPixKeysByInactivationDateRange(start, end);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(inactivePixKey.getId(), response.get(0).id());
    }
}