package kr.hhplus.be.server.domain.product;

import java.util.List;

public interface ProductRepository {

    Product save(Product product);
    Product getById(Long productId);
    List<Product> findAllByIds(List<Long> productIds);
    void updateQuantity(List<Product> products);
    List<Product> findTopProducts(int limit);
}
