package com.bank.ib.trading.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Repository
public class TradeSequenceRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Long nextValue() {
        BigInteger val = (BigInteger) em
                .createNativeQuery("""
                SELECT next_val
                FROM trade_sequence
                WHERE id = 1
                FOR UPDATE
            """)
                .getSingleResult();

        em.createNativeQuery("""
            UPDATE trade_sequence
            SET next_val = next_val + 1
            WHERE id = 1
        """).executeUpdate();

        return val.longValue();
    }

    @Transactional
    public Long getOrCreateAndLock(LocalDate tradeDate) {

            // 1️⃣ Try to lock existing row
        List<Long> result = em.createNativeQuery("""
            SELECT next_val
            FROM trade_sequence_daily
            WHERE trade_date = :tradeDate
            FOR UPDATE
        """).setParameter("tradeDate", tradeDate)
                    .getResultList();

            // 2️⃣ If no row exists, create one
            if (result.isEmpty()) {
                em.createNativeQuery("""
                INSERT INTO trade_sequence_daily (trade_date, next_val)
                VALUES (:tradeDate, 1)
            """).setParameter("tradeDate", tradeDate)
                        .executeUpdate();

                // Lock newly inserted row
                result = em.createNativeQuery("""
                SELECT next_val
                FROM trade_sequence_daily
                WHERE trade_date = :tradeDate
                FOR UPDATE
            """).setParameter("tradeDate", tradeDate)
                        .getResultList();
            }

           // BigInteger current =  result.get(0);

            // 3️⃣ Increment sequence
        em.createNativeQuery("""
            UPDATE trade_sequence_daily
            SET next_val = next_val + 1
            WHERE trade_date = :tradeDate
        """).setParameter("tradeDate", tradeDate)
                    .executeUpdate();

            // 4️⃣ Return original value
            return result.get(0);
        }


}

