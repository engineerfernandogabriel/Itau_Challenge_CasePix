package br.com.itau.CasePix.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pix_keys")
public class PixKey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PixKeyType keyType;

    @Column(nullable = false, unique = true)
    private String keyValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BankAccountType bankAccountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Column(nullable = false)
    private String agencyNumber;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String ownerName;

    private String ownerLastName;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime inactivatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PixKeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(PixKeyType keyType) {
        this.keyType = keyType;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public BankAccountType getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(BankAccountType bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType AccountType) {
        this.accountType = AccountType;
    }

    public String getAgencyNumber() {
        return agencyNumber;
    }

    public void setAgencyNumber(String agencyNumber) {
        this.agencyNumber = agencyNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getInactivatedAt() {
        return inactivatedAt;
    }

    public void setInactivatedAt(LocalDateTime inactivatedAt) {
        this.inactivatedAt = inactivatedAt;
    }
}
