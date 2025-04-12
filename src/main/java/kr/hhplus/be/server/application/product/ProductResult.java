package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.Product;
import lombok.Getter;

public class ProductResult {

    @Getter
    public static class ProductInfoResult {
        private long productId;
        private String name;
        private long price;
        private int quantity;

        public ProductInfoResult(Product product) {
            this.productId = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.quantity = product.getQuantity();
        }
    }
}
