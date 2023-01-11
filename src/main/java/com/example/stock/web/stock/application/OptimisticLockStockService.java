package com.example.stock.web.stock.application;

import com.example.stock.domain.stock.Stock;
import com.example.stock.web.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptimisticLockStockService {

    private StockRepository stockRepository;

    public OptimisticLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decrease(Long productId, Long quantity) {
        Stock stock = stockRepository.findByProductIdWithOptimisticLock(productId);
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
