package com.example.stock.test.locktest.rdb;

import com.example.stock.common.AbstractTestContainer;
import com.example.stock.domain.stock.Stock;
import com.example.stock.web.stock.application.PessimisticLockStockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("비관적 락 통합 테스트")
class PessimisticLockIntegrationTest extends AbstractTestContainer {

    @Autowired
    private PessimisticLockStockService pessimisticLockStockService;

    @Test
    @DisplayName("비관적 락을 사용했을때, 100개의 상품을 동시에 주문하면 재고가 100개 감소된다.")
    void 데이터베이스_비관적_락_통합_테스트() throws Exception {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(100);

        // when
        for (int index = 1; index <= 100; index++) {
            executorService.submit(() -> {
                try {
                    pessimisticLockStockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Stock stock = stockRepository.findStockByProductId(1L).orElseThrow();
        assertEquals(0, stock.getQuantity());
    }
}
