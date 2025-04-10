package kr.hhplus.be.server.domain.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {

    private Long id;
    private String name;
    private long price;
    private int quantity;

    public static Product create(@NonNull String name, long price, int quantity) {
        if (price <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 금액입니다. 최소 금액을 확인해주세요.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 수량입니다. 최소 수량을 확인해주세요.");
        }
        return new Product(null, name, price, quantity);
    }

    public static Product of(long id, @NonNull String name, long price, int quantity) {
        if (id <= 0) {
            throw new IllegalArgumentException("상품 식별자가 유효하지 않습니다.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 금액입니다. 최소 금액을 확인해주세요.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("유효하지 않은 상품 수량입니다. 상품 수량은 음수일 수 없습니다.");
        }
        return new Product(id, name, price, quantity);
    }

    // increase(), decrease() 에서 검사하므로 해당 메소드로 별도의 테스트는 작성하지 않았습니다.
    public boolean isNotSufficient(int amount) {
        return quantity < amount;
    }

    public void increaseStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("재고 추가 수량이 유효하지 않습니다.");
        }
        this.quantity += amount;
    }

    public void decreaseStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("재고 차감 수량이 유효하지 않습니다.");
        }
        if (quantity - amount < 0) {
            throw new IllegalArgumentException("재고보다 많은 수량을 차감할 수 없습니다.");
        }
        this.quantity -= amount;
    }
}
