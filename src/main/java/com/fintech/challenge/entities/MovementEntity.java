package com.fintech.challenge.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movement")
public class MovementEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private WalletEntity wallet;

    private String type;

    private Double amount;

    private LocalDateTime createdAt;

    private String reference;

    private Long relatedMovementId;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Long getRelatedMovementId() {
        return relatedMovementId;
    }

    public void setRelatedMovementId(Long relatedMovementId) {
        this.relatedMovementId = relatedMovementId;
    }
}
