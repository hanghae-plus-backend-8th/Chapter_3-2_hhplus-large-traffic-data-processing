package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.application.product.ProductResult.ProductInfoResult;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.shared.exception.NotFoundResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private ProductRepository productRepository;

    public ProductInfoResult info(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundResourceException("조회되는 상품이 없습니다."));

        return new ProductInfoResult(product);
    }
}
