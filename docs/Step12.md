## ✍️ 조회 기능 리스트업

- 포인트 잔액 조회
- 보유 쿠폰 목록 조회
- 상품 조회
- 인기 상품 조회

<br>

## ❓ 예상 병목 지점

- 포인트 잔액 조회 (❌)
    
    - 클러스터링 인덱스 기반으로 조회하기 때문에 매우 빠른 조회 가능
    - 별도로 고유한 사용자 식별자를 사용한다면 Unique 인덱스 설정하여 해결 (데이터 일관성 + 조회 성능)
    - e.g. UUID v4 (🐢) / UUID v7 (🚀)

- 보유 쿠폰 목록 조회 (❌)

    - 논 클러스터링 인덱스 기반으로 상품을 조회하기 때문에 빠른 조회 가능
    - 별도로 고유한 사용자 식별자를 사용한다면 Unique 인덱스 설정하여 해결 (데이터 일관성 + 조회 성능)
    - e.g. UUID v4 (🐢) / UUID v7 (🚀)

- 상품 조회 (❌)

    - 클러스터링 인덱스 기반으로 조회하기 때문에 매우 빠른 조회 가능
    - 별도로 고유한 사용자 식별자를 사용한다면 Unique 인덱스 설정하여 해결 (데이터 일관성 + 조회 성능)
    - e.g. UUID v4 (🐢) / UUID v7 (🚀)

- 인기 상품 조회 (✅)

    - 주문 통계 테이블에서 복잡한 쿼리를 통해 데이터를 가져오기 때문에 병목이 발생할 가능성이 매우 높음

<br>

## 💡 캐싱 적용

### 📌 적용 대상

- 인기 상품 조회 API
  - 인기 상품 조회의 경우, 일간으로 갱신되어 "자주 변경되지 않는 데이터" 이므로 캐싱에 적합
  - 상품별 집계를 위해 GROUP BY 연산을 사용하고 이로 인해 임시 테이블을 생성하기 때문에 데이터베이스에 많은 부하 발생
  - 확실한 성능 비교를 위해 Join 연산을 제거하지 않고 진행 (e.g. Join 연산 제거 후, product_id 식별자로 재조회 X)
```sql
SELECT
    P.product_id
    , P.name
    , P.price
    , P.quantity
FROM
    ORDER_STATISTICS OS
INNER JOIN
    PRODUCT P
ON 
    OS.product_id = P.product_id
WHERE
    OS.created_at >= '2025-05-01 00:00:00'
GROUP BY
    OS.product_id
ORDER BY
    SUM(OS.quantity) DESC
LIMIT
    10;
```

<br>

### 🔹 캐싱 전략

- 읽기 전략
    - Read Through 전략을 활용하여 캐시 스탬피드 현상 방지
        ```java
        public List<Product> findTopProductsCaching(int limit) {
            String jsonData = redisTemplate.opsForValue()
                    .get("topProducts");
    
            if (jsonData == null) {
                return new ArrayList<>();
            }
            return JsonUtil.fromJson(jsonData, new TypeReference<List<Product>>() {});
        }
        ```

- 쓰기 전략
    - Cache Preload 전략을 활용하여 주기적으로 인기 상품 데이터 갱신 (일간)
        ```java
        @Scheduled(cron = "0 0 1 * * *")
        public void popularProduct() {
            List<Product> topProducts = productRepository.findTopProducts(10);
            String key = "topProducts";
            String value = JsonUtil.toJson(topProducts);
  
            // 캐시 오버랩 구간 설정
            redisTemplate.opsForValue()
                .set(key, value, Duration.ofHours(25L));
        }
        ```
<br>

## 📊 BMT 진행

### 🛠 테스트 환경

- MacOS M4 Pro (OS 15.4.1)
- Spring Boot (3.4.1)
- MySQL 8

<br>

### ♻️ 데이터 셋업

- JPA Stateless Session + Instancio 라이브러리 활용하여 상품, 주문, 주문통계 데이터 셋업 (약 100만건)

```java
@Slf4j
@Component
@RequiredArgsConstructor
public class BatchComponent {
    private final EntityManagerFactory entityManagerFactory;

    @EventListener(ApplicationReadyEvent.class)
    public void batch() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        StatelessSession statelessSession = sessionFactory.openStatelessSession();
        Transaction tx = statelessSession.beginTransaction();
    
        try {
            for (int i = 1; i < 1_000_000; i++) {
                LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
                LocalDateTime end = LocalDateTime.of(2025, 12, 31, 23, 59);
                ProductEntity productEntity = Instancio.of(ProductEntity.class)
                        .set(field(ProductEntity::getProductId), (long) i)
                        .generate(field(ProductEntity::getName), gen -> gen.string().prefix("상품").length(20))
                        .generate(field(ProductEntity::getPrice), gen -> gen.longs().min(10000L).max(300000L))
                        .generate(field(ProductEntity::getQuantity), gen -> gen.ints().min(10).max(30))
                        .generate(field(ProductEntity::getCreatedAt), gen -> gen.temporal().localDateTime().range(start, end))
                        .create();
        
                statelessSession.insert(productEntity);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            statelessSession.close();
        }
    }
}
```

<br>

### 📈 캐싱 적용전

- 톰캣 서버가 1대이므로 동시 요청은 DB 커넥션풀 개수 이하로 설정하여 진행

| Concurrency | Total Requests | Min Latency (ms) | Max Latency (ms) | Avg Latency (ms) |
|:-----------:|:--------------:|:----------------:|:----------------:|:----------------:|
|      3      |       3        |       9120       |       9204       |       9179       |
|      3      |       6        |       9295       |      18418       |      13307       |
|      3      |       9        |       9187       |      27634       |      18440       |

<br>

### 📉 캐싱 적용후

| Concurrency | Total Requests | Min Latency (ms) | Max Latency (ms) | Avg Latency (ms) |
|:-----------:|:--------------:|:----------------:|:----------------:|:----------------:|
|      3      |       3        |        34        |        36        |        35        |
|      3      |       6        |        35        |        36        |        35        |
|      3      |       9        |        34        |        37        |        35        |

<br>


## 🔍 결론

- Redis 기반의 캐싱 전략을 도입함으로써 API 응답 시간을 획기적으로 단축할 수 있었습니다.

- Redis 서버를 Master-Slave 구조로 운영할 때, 트래픽이 증가할수록 마스터 노드에 부하가 집중되기 때문에 이를 해소하기 위한
  Redis 클러스터 구성을 생각해봤습니다. (온프레미스 기준)

- 다만, Redis 클러스터 구성은 인프라 복잡도가 증가하므로 로컬 캐시를 활용한 2계층 캐시 전략 (2-Layered Caching)을 적용하면
  좋을 것 같습니다.

- 온프레미스에서 Redis 서버를 구축할 시, 적절한 maxmemory, maxmemory-policy 옵션을 설정하여 OOM 이슈를 방지해야할 것 같습니다.
