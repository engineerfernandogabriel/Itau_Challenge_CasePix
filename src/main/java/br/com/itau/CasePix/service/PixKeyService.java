package br.com.itau.CasePix.service;

import br.com.itau.CasePix.dto.PixKeyRequestDTO;
import br.com.itau.CasePix.dto.PixKeyResponseDTO;
import br.com.itau.CasePix.model.PixKey;
import br.com.itau.CasePix.model.PixKeyType;
import br.com.itau.CasePix.repository.PixKeyRepository;
import br.com.itau.CasePix.util.PixKeyValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class PixKeyService {
    private final PixKeyRepository pixKeyRepository;
    private final PixKeyValidator pixKeyValidator;

    @Autowired
    public PixKeyService(PixKeyRepository pixKeyRepository, PixKeyValidator pixKeyValidator) {
        this.pixKeyRepository = pixKeyRepository;
        this.pixKeyValidator = pixKeyValidator;
    }

    @Transactional
    public PixKeyResponseDTO createPixKey(PixKeyRequestDTO request) {
        pixKeyValidator.validateKeyLimit(request.bankAccountType(), request.accountNumber());
        pixKeyValidator.validatePixKeyRequest(request);
        PixKey pixKey = mapRequestToPixKey(request);
        pixKey.setCreatedAt(LocalDateTime.now());
        pixKey = pixKeyRepository.save(pixKey);
        return mapPixKeyToResponse(pixKey);
    }

    @Transactional
    public PixKeyResponseDTO updatePixKey(UUID id, PixKeyRequestDTO request) {
        PixKey pixKey = pixKeyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chave Pix não encontrada"));

        if (pixKey.getInactivatedAt() != null) {
            throw new IllegalStateException("Não é possível alterar uma chave inativada");
        }

        validateUpdateRequest(pixKey, request);

        updatePixKeyFromRequest(pixKey, request);
        pixKey = pixKeyRepository.save(pixKey);
        return mapPixKeyToResponse(pixKey);
    }

    @Transactional
    public PixKeyResponseDTO inactivatePixKey(UUID id) {
        PixKey pixKey = pixKeyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chave Pix não encontrada"));

        if (pixKey.getInactivatedAt() != null) {
            throw new IllegalStateException("Chave Pix já inativa");
        }

        pixKey.setInactivatedAt(LocalDateTime.now());
        pixKey = pixKeyRepository.save(pixKey);
        return mapPixKeyToResponse(pixKey);
    }

    public PixKeyResponseDTO getPixKeyById(UUID id) {
        PixKey pixKey = pixKeyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chave Pix não encontrada"));
        return mapPixKeyToResponse(pixKey);
    }

    public List<PixKeyResponseDTO> getPixKeysByType(PixKeyType keyType) {
        List<PixKey> pixKeys = pixKeyRepository.findByKeyType(keyType);
        return pixKeys.stream().map(this::mapPixKeyToResponse).collect(toList());
    }

    public List<PixKeyResponseDTO> getPixKeysByAgencyAndAccount(String agencyNumber, String accountNumber) {
        List<PixKey> pixKeys = pixKeyRepository.findByAgencyNumberAndAccountNumber(agencyNumber, accountNumber);
        return pixKeys.stream().map(this::mapPixKeyToResponse).collect(toList());
    }

    public List<PixKeyResponseDTO> getPixKeysByOwnerName(String ownerName) {
        List<PixKey> pixKeys = pixKeyRepository.findByOwnerNameContainingIgnoreCase(ownerName);
        return pixKeys.stream().map(this::mapPixKeyToResponse).collect(toList());
    }

    public List<PixKeyResponseDTO> getPixKeysByCreationDateRange(LocalDateTime start, LocalDateTime end) {
        List<PixKey> pixKeys = pixKeyRepository.findByCreatedAtBetween(start, end);
        return pixKeys.stream().map(this::mapPixKeyToResponse).collect(toList());
    }

    public List<PixKeyResponseDTO> getPixKeysByInactivationDateRange(LocalDateTime start, LocalDateTime end) {
        List<PixKey> pixKeys = pixKeyRepository.findByInactivatedAtBetween(start, end);
        return pixKeys.stream().map(this::mapPixKeyToResponse).collect(toList());
    }

    private void validateUpdateRequest(PixKey pixKey, PixKeyRequestDTO request) {
        if (!pixKey.getKeyType().equals(request.keyType())) {
            throw new IllegalArgumentException("O tipo da chave não pode ser alterado");
        }

        if (!pixKey.getKeyValue().equals(request.keyValue()) && pixKeyRepository.existsByKeyValue(request.keyValue())) {
            throw new IllegalArgumentException("Nova chave Pix já cadastrada");
        }

        pixKeyValidator.validatePixKeyRequest(request);
    }

    private PixKey mapRequestToPixKey(PixKeyRequestDTO request) {
        PixKey pixKey = new PixKey();
        pixKey.setKeyType(request.keyType());
        pixKey.setKeyValue(request.keyValue());
        pixKey.setBankAccountType(request.bankAccountType());
        pixKey.setAccountType(request.accountType());
        pixKey.setAgencyNumber(request.agencyNumber());
        pixKey.setAccountNumber(request.accountNumber());
        pixKey.setOwnerName(request.ownerName());
        pixKey.setOwnerLastName(request.ownerLastName());
        return pixKey;
    }

    private void updatePixKeyFromRequest(PixKey pixKey, PixKeyRequestDTO request) {
        pixKey.setKeyValue(request.keyValue());
        pixKey.setBankAccountType(request.bankAccountType());
        pixKey.setAgencyNumber(request.agencyNumber());
        pixKey.setAccountNumber(request.accountNumber());
        pixKey.setOwnerName(request.ownerName());
        pixKey.setOwnerLastName(request.ownerLastName());
    }

    private PixKeyResponseDTO mapPixKeyToResponse(PixKey pixKey) {
        return new PixKeyResponseDTO(
                pixKey.getId(),
                pixKey.getKeyType(),
                pixKey.getKeyValue(),
                pixKey.getBankAccountType(),
                pixKey.getAccountType(),
                pixKey.getAgencyNumber(),
                pixKey.getAccountNumber(),
                pixKey.getOwnerName(),
                pixKey.getOwnerLastName(),
                pixKey.getCreatedAt(),
                pixKey.getInactivatedAt()
        );
    }
}