package com.fintech.challenge.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="wallet", indexes = {
        @Index(name="user_currency_country", columnList = "userId, currency, country", unique = true),
        @Index(name="user", columnList = "userId")
})
public class WalletEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String userId;

    private String currency;

    private String country;

    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
