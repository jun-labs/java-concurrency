package com.example.stock.web.stock.repository;

import com.example.stock.domain.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.productId = :productId")
    Stock findByIdWithPessimisticLock(@Param("productId") Long productId);

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("SELECT s FROM Stock s WHERE s.productId = :productId")
    Stock findByProductIdWithOptimisticLock(@Param("productId") Long productId);

    @Query("SELECT s FROM Stock s WHERE s.productId = :productId")
    Optional<Stock> findStockByProductId(@Param("productId") Long productId);
}
