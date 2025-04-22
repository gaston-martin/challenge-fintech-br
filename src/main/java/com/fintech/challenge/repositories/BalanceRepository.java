package com.fintech.challenge.repositories;

import com.fintech.challenge.entities.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {
}
