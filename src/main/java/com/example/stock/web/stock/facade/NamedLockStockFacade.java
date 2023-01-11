package com.example.stock.web.stock.facade;

import com.example.stock.web.stock.repository.LockRepository;
import com.example.stock.web.stock.application.StockService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NamedLockStockFacade {

    private final LockRepository lockRepository;
    private final StockService stockService;

    public NamedLockStockFacade(LockRepository lockRepository, StockService stockService) {
        this.lockRepository = lockRepository;
        this.stockService = stockService;
    }

    @Transactional
    public void decrease(Long productId, Long quantity) {
        try {
            lockRepository.getLockByProductId(productId.toString());
            stockService.decrease(productId, quantity);
        } finally {
            lockRepository.releaseLockByProductId(productId.toString());
        }
    }
}
