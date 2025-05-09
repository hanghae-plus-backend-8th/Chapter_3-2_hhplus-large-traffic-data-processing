package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.application.product.ProductResult.ProductInfoResult;
import kr.hhplus.be.server.application.product.ProductResult.ProductPopularResult;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductInfoResult info(long productId) {
        Product product = productRepository.getById(productId);

        return new ProductInfoResult(product);
    }

    public List<ProductPopularResult> popular(int limit) {
        return productRepository.findTopProductsCaching(limit)
                .stream()
                .map(ProductPopularResult::new)
                .toList();
    }
}
