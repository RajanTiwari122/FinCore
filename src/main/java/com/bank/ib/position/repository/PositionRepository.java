package com.bank.ib.position.repository;

import com.bank.ib.position.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository
        extends JpaRepository<Position, Long> {

    Optional<Position> findByIsinAndBook(String isin, String book);
}
