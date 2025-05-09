package kr.hhplus.be.server.interfaces.schedular.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.shared.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.util.List;

@Slf4j
//@Component (필요 시, 주석 제거)
@RequiredArgsConstructor
public class ProductSchedular {

    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    // 매일 새벽 1시에 배치 실행!
    @Scheduled(cron = "0 0 1 * * *")
    public void popularProduct() {
        List<Product> topProducts = productRepository.findTopProducts(10);
        String key = "topProducts";
        String value = JsonUtil.toJson(topProducts);

        // 캐시 오버랩 구간 설정
        redisTemplate.opsForValue()
                .set(key, value, Duration.ofHours(25L));
    }
}
