package com.bank.ib.marketdata.repository;

import com.bank.ib.reference.security_mapping.SecuritySymbolMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecuritySymbolMappingRepository extends JpaRepository<SecuritySymbolMapping, String> {
    @Query("SELECT ssm FROM SecuritySymbolMapping ssm WHERE ssm.isin IN :isins")
    List<SecuritySymbolMapping> findByIsins(@Param("isins") List<String> isins);

}
