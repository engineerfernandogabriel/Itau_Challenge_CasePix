package br.com.itau.CasePix.repository;

import br.com.itau.CasePix.model.PixKey;
import br.com.itau.CasePix.model.PixKeyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PixKeyRepository extends JpaRepository<PixKey, UUID> {
    boolean existsByKeyValue(String keyValue);

    long countByAccountNumber(String accountNumber);

    List<PixKey> findByKeyType(PixKeyType keyType);

    List<PixKey> findByAgencyNumberAndAccountNumber(String agencyNumber, String accountNumber);

    List<PixKey> findByOwnerNameContainingIgnoreCase(String ownerName);

    List<PixKey> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<PixKey> findByInactivatedAtBetween(LocalDateTime start, LocalDateTime end);
}
