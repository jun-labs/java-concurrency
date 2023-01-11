package com.example.stock.test.locktest.redis;

import com.example.stock.common.AbstractTestContainer;
import com.example.stock.domain.stock.Stock;
import com.example.stock.web.stock.facade.LettuceLockStockFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class LettuceLockStockFacadeTest extends AbstractTestContainer {

    @Autowired
    private LettuceLockStockFacade lettuceLockStockFacade;

    @Test
    @DisplayName("Lettuce의 락을 사용했을때, 100개의 상품을 동시에 주문하면 재고가 100개 감소된다.")
    void Lettuce를_사용한_통합_테스트() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(100);

        // when
        for (int i = 1; i <= 100; i++) {
            executorService.submit(() -> {
                try {
                    lettuceLockStockFacade.decrease(1L, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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
