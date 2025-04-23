package com.fintech.challenge.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="balance")
public class BalanceEntity {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private WalletEntity wallet;

    private Double balance;

    private LocalDateTime updatedAt;

    private Long lastMovementId;

    @Version
    private Long version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WalletEntity getWallet() {
        return wallet;
    }

    public void setWallet(WalletEntity wallet) {
        this.wallet = wallet;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getLastMovementId() {
        return lastMovementId;
    }

    public void setLastMovementId(Long lastMovementId) {
        this.lastMovementId = lastMovementId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
