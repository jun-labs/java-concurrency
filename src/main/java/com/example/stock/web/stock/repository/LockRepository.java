package com.example.stock.web.stock.repository;

import com.example.stock.domain.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LockRepository extends JpaRepository<Stock, Long> {

    @Query(value = "SELECT get_lock(:productId, 3000)", nativeQuery = true)
    void getLockByProductId(@Param("productId") String productId);

    @Query(value = "SELECT release_lock(:productId)", nativeQuery = true)
    void releaseLockByProductId(@Param("productId") String productId);
}
