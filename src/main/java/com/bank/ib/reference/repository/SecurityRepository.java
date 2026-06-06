package com.bank.ib.reference.repository;

import com.bank.ib.reference.entity.Security;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityRepository extends JpaRepository<Security, String> {
    public Optional<Security> findByIsin(String isin);
    List<Security> findByActiveTrue();
}
