package com.example.stock.test.failuretest;

import com.example.stock.common.AbstractTestContainer;
import com.example.stock.domain.stock.Stock;
import com.example.stock.web.stock.application.StockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("멀티쓰레드 환경 실패 통합테스트")
class StockServiceTest extends AbstractTestContainer {

    @Autowired
    private StockService stockService;

    @Test
    void decrease_test() {
        // when
        stockService.decrease(1L, 1L);

        // then
        Stock stock = stockRepository.findStockByProductId(1L).orElseThrow();
        assertEquals(99, stock.getQuantity());
    }

    @Test
    @DisplayName("멀티쓰레드 환경에서 100명이 동시에 주문을 하면 RaceCondition이 발생해서 재고가 0이 되지 않을 수 있다.")
    void 멀티쓰레드_환경에서_재고감소_통합_테스트() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(100);

        // when
        for (int i = 1; i <= 100; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // then
        Stock stock = stockRepository.findStockByProductId(1L).orElseThrow();
        assertNotEquals(0, stock.getQuantity());
    }
}
