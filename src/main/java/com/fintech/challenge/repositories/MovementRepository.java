package com.fintech.challenge.repositories;

import com.fintech.challenge.entities.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface MovementRepository extends JpaRepository<MovementEntity, Long> {

    @Query("SELECT COALESCE(SUM(m.amount),0) FROM MovementEntity m WHERE m.wallet.id = :walletId AND m.createdAt <= :pointInTime")
    Double findSumByWalletIdAndPointInTime(Long walletId, LocalDateTime pointInTime);
}
