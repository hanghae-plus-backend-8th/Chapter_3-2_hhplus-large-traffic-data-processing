package kr.hhplus.be.server.infrastructure.product.persistence;

import java.util.List;

public interface ProductJpaRepositoryCustom {

    List<ProductEntity> findTopProducts(int limit);
}
