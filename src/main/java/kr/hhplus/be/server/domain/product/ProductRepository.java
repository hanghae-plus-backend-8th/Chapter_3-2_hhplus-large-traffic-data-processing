package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.order.OrderProduct;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);
    Optional<Product> findById(Long productId);
    List<Product> findAllByIds(List<Long> productIds);
    void updateQuantity(List<OrderProduct> orderProducts);
}
