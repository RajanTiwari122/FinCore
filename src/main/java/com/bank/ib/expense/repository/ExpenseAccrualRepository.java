package com.bank.ib.expense.repository;

import com.bank.ib.expense.entity.ExpenseAccrual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseAccrualRepository extends JpaRepository<ExpenseAccrual, Long> {
    List<ExpenseAccrual> findByFundId(String fundId);
}
