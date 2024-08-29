package br.com.itau.CasePix.controller;

import br.com.itau.CasePix.dto.PixKeyRequestDTO;
import br.com.itau.CasePix.dto.PixKeyResponseDTO;
import br.com.itau.CasePix.model.PixKeyType;
import br.com.itau.CasePix.service.PixKeyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pix-keys")
public class PixKeyController {
    private final PixKeyService pixKeyService;

    @Autowired
    public PixKeyController(PixKeyService pixKeyService) {
        this.pixKeyService = pixKeyService;
    }

    @PostMapping
    public ResponseEntity<PixKeyResponseDTO> createPixKey(@Valid @RequestBody PixKeyRequestDTO request) {
        PixKeyResponseDTO response = pixKeyService.createPixKey(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePixKey(@PathVariable UUID id, @Valid @RequestBody PixKeyRequestDTO request) {
        try {
            PixKeyResponseDTO response = pixKeyService.updatePixKey(id, request);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chave Pix não encontrada");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> inactivatePixKey(@PathVariable UUID id) {
        try {
            PixKeyResponseDTO response = pixKeyService.inactivatePixKey(id);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chave Pix não encontrada");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Chave Pix já está inativa");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PixKeyResponseDTO> getPixKeyById(@PathVariable UUID id) {
        try {
            PixKeyResponseDTO response = pixKeyService.getPixKeyById(id);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-keyType")
    public ResponseEntity<List<PixKeyResponseDTO>> getPixKeysByType(@RequestParam PixKeyType type) {
        List<PixKeyResponseDTO> pixKeys = pixKeyService.getPixKeysByType(type);
        return ResponseEntity.ok(pixKeys);
    }

    @GetMapping("/by-agency-and-account")
    public ResponseEntity<List<PixKeyResponseDTO>> getPixKeysByAgencyAndAccount(
            @RequestParam String agencyNumber,
            @RequestParam String accountNumber) {
        List<PixKeyResponseDTO> pixKeys = pixKeyService.getPixKeysByAgencyAndAccount(agencyNumber, accountNumber);
        return ResponseEntity.ok(pixKeys);
    }

    @GetMapping("/by-owner-name")
    public ResponseEntity<List<PixKeyResponseDTO>> getPixKeysByOwnerName(@RequestParam String ownerName) {
        List<PixKeyResponseDTO> pixKeys = pixKeyService.getPixKeysByOwnerName(ownerName);
        return ResponseEntity.ok(pixKeys);
    }

    @GetMapping("/by-creation-date")
    public ResponseEntity<List<PixKeyResponseDTO>> getPixKeysByCreationDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<PixKeyResponseDTO> pixKeys = pixKeyService.getPixKeysByCreationDateRange(start, end);
        return ResponseEntity.ok(pixKeys);
    }

    @GetMapping("/by-inactivation-date")
    public ResponseEntity<List<PixKeyResponseDTO>> getPixKeysByInactivationDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<PixKeyResponseDTO> pixKeys = pixKeyService.getPixKeysByInactivationDateRange(start, end);
        return ResponseEntity.ok(pixKeys);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }
}
