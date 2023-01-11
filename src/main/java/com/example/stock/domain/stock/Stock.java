package com.example.stock.domain.stock;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.Objects;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @Column
    private Long productId;

    @Column
    private Long quantity;

    @Version
    private Long version;

    /**
     * @Nullary-Constructor. JPA 기본 생성자로 stock 패키지 외부에서 호출하지 말 것.
     */
    public Stock() {
    }

    public void decrease(Long quantity) {
        if (this.quantity - quantity < 0) {
            throw new RuntimeException("상품 재고가 존재하지 않습니다.");
        }
        this.quantity -= quantity;
    }

    public void decreaseWithSynchronize(Long quantity) {
        if (this.quantity - quantity < 0) {
            throw new RuntimeException("상품 재고가 존재하지 않습니다.");
        }
        this.quantity -= quantity;
    }

    public Stock(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock)) return false;
        Stock stock = (Stock) o;
        return stockId.equals(stock.stockId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockId);
    }

    @Override
    public String toString() {
        return String.format("Id: %s, Stock: %s", stockId, quantity);
    }
}
