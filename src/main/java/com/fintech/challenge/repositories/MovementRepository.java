package com.fintech.challenge.repositories;

import com.fintech.challenge.entities.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementRepository extends JpaRepository<MovementEntity, Long> {
}
