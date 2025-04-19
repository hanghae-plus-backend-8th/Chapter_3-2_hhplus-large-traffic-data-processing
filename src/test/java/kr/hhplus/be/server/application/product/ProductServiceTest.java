package kr.hhplus.be.server.application.product;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.product.ProductResult.ProductInfoResult;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Transactional
@SpringBootTest
class ProductServiceTest {

    @MockitoSpyBean
    private ProductService productService;

    @MockitoSpyBean
    private ProductRepository productRepository;

    @DisplayName("사용자는 상품을 조회할 수 있다.")
    @Test
    void getProductInfo() {
        // given
        Product product = Product.create("상품명", 3000L, 50);
        product = productRepository.save(product);

        // when
        ProductInfoResult productInfoResult = productService.info(product.getId());

        // then
        assertThat(productInfoResult).extracting("name", "price", "quantity")
                .containsExactlyInAnyOrder(product.getName(), product.getPrice(), product.getQuantity());
        verify(productRepository, times(1)).getById(product.getId());
    }
}