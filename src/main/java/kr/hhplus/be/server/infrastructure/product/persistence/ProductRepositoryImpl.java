package kr.hhplus.be.server.infrastructure.product.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.shared.exception.NotFoundResourceException;
import kr.hhplus.be.server.shared.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = ProductEntity.builder()
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();

        return productJpaRepository.save(productEntity)
                .toDomain();
    }

    @Override
    public Product getById(Long productId) {
        return productJpaRepository.findById(productId)
                .orElseThrow(() -> new NotFoundResourceException("조회되는 상품이 없습니다."))
                .toDomain();
    }

    @Override
    public void update(Product product) {
        ProductEntity productEntity = productJpaRepository.findById(product.getId())
                .orElseThrow(() -> new NotFoundResourceException("조회되는 상품이 없습니다."));

        productEntity.update(product);
    }

    @Override
    public List<Product> findAllByIds(List<Long> productIds) {
        return productJpaRepository.findAllById(productIds)
                .stream()
                .map(ProductEntity::toDomain)
                .toList();
    }

    @Override
    public List<Product> findAllByIdsLocking(List<Long> productIds) {
        return productJpaRepository.findAllByIdsLocking(productIds)
                .stream()
                .map(ProductEntity::toDomain)
                .toList();
    }

    @Override
    public List<Product> findTopProducts(int limit) {
        return productJpaRepository.findTopProducts(limit)
                .stream()
                .map(ProductEntity::toDomain)
                .toList();
    }

    @Override
    public List<Product> findTopProductsCaching(int limit) {
        String jsonData = redisTemplate.opsForValue()
                .get("topProducts");

        if (jsonData == null) {
            return new ArrayList<>();
        }
        return JsonUtil.fromJson(jsonData, new TypeReference<List<Product>>() {});
    }
}