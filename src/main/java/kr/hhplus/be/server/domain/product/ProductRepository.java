package kr.hhplus.be.server.domain.product;

import java.util.List;

public interface ProductRepository {

    Product save(Product product);
    Product getById(Long productId);
    void update(Product product);
    List<Product> findAllByIds(List<Long> productIds);
    List<Product> findAllByIdsLocking(List<Long> productIds);
    List<Product> findTopProducts(int limit);
}
