package br.com.itau.CasePix.controller;

import br.com.itau.CasePix.dto.PixKeyRequestDTO;
import br.com.itau.CasePix.dto.PixKeyResponseDTO;
import br.com.itau.CasePix.model.AccountType;
import br.com.itau.CasePix.model.BankAccountType;
import br.com.itau.CasePix.model.PixKeyType;
import br.com.itau.CasePix.service.PixKeyService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PixKeyControllerTest {

    @Mock
    private PixKeyService pixKeyService;

    @InjectMocks
    private PixKeyController pixKeyController;

    private PixKeyRequestDTO requestDTO;
    private PixKeyResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDTO = new PixKeyRequestDTO(
                PixKeyType.CPF,
                "12345678901",
                BankAccountType.PESSOA_FISICA,
                AccountType.CORRENTE,
                "1234",
                "12345678",
                "John",
                "Doe"
        );

        responseDTO = new PixKeyResponseDTO(
                UUID.randomUUID(),
                PixKeyType.CPF,
                "12345678901",
                BankAccountType.PESSOA_FISICA,
                AccountType.CORRENTE,
                "1234",
                "12345678",
                "John",
                "Doe",
                LocalDateTime.now(),
                null
        );
    }

    @Test
    void createPixKey_Success() {
        when(pixKeyService.createPixKey(any(PixKeyRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<PixKeyResponseDTO> response = pixKeyController.createPixKey(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(pixKeyService, times(1)).createPixKey(requestDTO);
    }

    @Test
    void updatePixKey_Success() {
        UUID id = UUID.randomUUID();
        when(pixKeyService.updatePixKey(eq(id), any(PixKeyRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<?> response = pixKeyController.updatePixKey(id, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(pixKeyService, times(1)).updatePixKey(id, requestDTO);
    }

    @Test
    void updatePixKey_NotFound() {
        UUID id = UUID.randomUUID();
        when(pixKeyService.updatePixKey(eq(id), any(PixKeyRequestDTO.class)))
                .thenThrow(new EntityNotFoundException("Chave Pix não encontrada"));

        ResponseEntity<?> response = pixKeyController.updatePixKey(id, requestDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Chave Pix não encontrada", response.getBody());
    }

    @Test
    void updatePixKey_IllegalState() {
        UUID id = UUID.randomUUID();
        when(pixKeyService.updatePixKey(eq(id), any(PixKeyRequestDTO.class)))
                .thenThrow(new IllegalStateException("Não é possível alterar uma chave inativada"));

        ResponseEntity<?> response = pixKeyController.updatePixKey(id, requestDTO);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Não é possível alterar uma chave inativada", response.getBody());
    }

    @Test
    void inactivatePixKey_Success() {
        UUID id = UUID.randomUUID();
        when(pixKeyService.inactivatePixKey(id)).thenReturn(responseDTO);

        ResponseEntity<?> response = pixKeyController.inactivatePixKey(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(pixKeyService, times(1)).inactivatePixKey(id);
    }

    @Test
    void inactivatePixKey_NotFound() {
        UUID id = UUID.randomUUID();
        when(pixKeyService.inactivatePixKey(id)).thenThrow(new EntityNotFoundException("Chave Pix não encontrada"));

        ResponseEntity<?> response = pixKeyController.inactivatePixKey(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Chave Pix não encontrada", response.getBody());
    }

    @Test
    void inactivatePixKey_AlreadyInactive() {
        UUID id = UUID.randomUUID();
        when(pixKeyService.inactivatePixKey(id)).thenThrow(new IllegalStateException("Chave Pix já está inativa"));

        ResponseEntity<?> response = pixKeyController.inactivatePixKey(id);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Chave Pix já está inativa", response.getBody());
    }

    @Test
    void getPixKeyById_Success() {
        UUID id = UUID.randomUUID();
        when(pixKeyService.getPixKeyById(id)).thenReturn(responseDTO);

        ResponseEntity<PixKeyResponseDTO> response = pixKeyController.getPixKeyById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(pixKeyService, times(1)).getPixKeyById(id);
    }

    @Test
    void getPixKeyById_NotFound() {
        UUID id = UUID.randomUUID();
        when(pixKeyService.getPixKeyById(id)).thenThrow(new EntityNotFoundException());

        ResponseEntity<PixKeyResponseDTO> response = pixKeyController.getPixKeyById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getPixKeysByType_Success() {
        List<PixKeyResponseDTO> expectedList = Arrays.asList(responseDTO);
        when(pixKeyService.getPixKeysByType(PixKeyType.CPF)).thenReturn(expectedList);

        ResponseEntity<List<PixKeyResponseDTO>> response = pixKeyController.getPixKeysByType(PixKeyType.CPF);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(pixKeyService, times(1)).getPixKeysByType(PixKeyType.CPF);
    }

    @Test
    void getPixKeysByAgencyAndAccount_Success() {
        List<PixKeyResponseDTO> expectedList = Arrays.asList(responseDTO);
        when(pixKeyService.getPixKeysByAgencyAndAccount("1234", "12345678")).thenReturn(expectedList);

        ResponseEntity<List<PixKeyResponseDTO>> response = pixKeyController.getPixKeysByAgencyAndAccount("1234", "12345678");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(pixKeyService, times(1)).getPixKeysByAgencyAndAccount("1234", "12345678");
    }

    @Test
    void getPixKeysByOwnerName_Success() {
        List<PixKeyResponseDTO> expectedList = Arrays.asList(responseDTO);
        when(pixKeyService.getPixKeysByOwnerName("John")).thenReturn(expectedList);

        ResponseEntity<List<PixKeyResponseDTO>> response = pixKeyController.getPixKeysByOwnerName("John");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(pixKeyService, times(1)).getPixKeysByOwnerName("John");
    }

    @Test
    void getPixKeysByCreationDateRange_Success() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<PixKeyResponseDTO> expectedList = Arrays.asList(responseDTO);
        when(pixKeyService.getPixKeysByCreationDateRange(start, end)).thenReturn(expectedList);

        ResponseEntity<List<PixKeyResponseDTO>> response = pixKeyController.getPixKeysByCreationDateRange(start, end);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(pixKeyService, times(1)).getPixKeysByCreationDateRange(start, end);
    }

    @Test
    void getPixKeysByInactivationDateRange_Success() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        List<PixKeyResponseDTO> expectedList = Arrays.asList(responseDTO);
        when(pixKeyService.getPixKeysByInactivationDateRange(start, end)).thenReturn(expectedList);

        ResponseEntity<List<PixKeyResponseDTO>> response = pixKeyController.getPixKeysByInactivationDateRange(start, end);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(pixKeyService, times(1)).getPixKeysByInactivationDateRange(start, end);
    }
}