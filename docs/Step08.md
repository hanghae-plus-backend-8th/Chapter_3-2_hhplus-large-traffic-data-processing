

## ✍️ 조회 기능 목록

- 포인트 잔액 조회
- 보유 쿠폰 목록 조회
- 상품 조회
- 인기 상품 조회

## ❓ 예상 병목 지점

- 포인트 잔액 조회 (❌)

    - 사용자 PK (클러스터링 인덱스) 기반으로 상품을 조회하기 때문에 병목 발생할 가능성 매우 적음
    - 별도로 고유한 사용자 식별자를 사용한다면 Unique 인덱스 설정하여 해결 (데이터 일관성 + 조회 성능)
    - e.g. UUID v4 (🐢) / UUID v7 (🚀)

- 보유 쿠폰 목록 조회 (❌)

    - 사용자 FK (논 클러스터링 인덱스) 기반으로 상품을 조회하기 때문에 병목 발생할 가능성 적음

- 상품 조회 (❌)

    - 상품 PK (클러스터링 인덱스) 기반으로 상품을 조회하기 때문에 병목 발생할 가능성 매우 적음
    - 별도로 고유한 상품번호를 사용한다면 Unique 인덱스 설정하여 해결 (데이터 일관성 + 조회 성능)
    - e.g. UUID v4 (🐢) / UUID v7 (🚀)

- 인기 상품 조회 (✅)

    - 주문 통계 테이블에서 복잡한 쿼리를 통해 가져오기 때문에 데이터가 쌓이면 병목 발생할 가능성 매우 농후

## 데이터 세팅

- JPA의 Stateless Session + instancio 라이브러리 활용
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

## 테스트 진행

### SQL
```mysql
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
    OS.created_at >= '2020-01-01 00:00:00'
GROUP BY
    OS.product_id
ORDER BY
    SUM(OS.quantity) DESC
LIMIT
    100;
```

### [인덱스 적용 전] 실행 계획
- 인덱스를 사용하지 않고 테이블 풀 스캔하여 검색 --> 9.498s 소요
```
id|select_type|table|partitions|type  |possible_keys                  |key    |key_len|ref                 |rows   |filtered|Extra                                       |
--+-----------+-----+----------+------+-------------------------------+-------+-------+--------------------+-------+--------+--------------------------------------------+
 1|SIMPLE     |OS   |          |ALL   |order_statistics_product_id_IDX|       |       |                    |1819232|   33.33|Using where; Using temporary; Using filesort|
 1|SIMPLE     |P    |          |eq_ref|PRIMARY                        |PRIMARY|8      |hhplus.OS.product_id|      1|   100.0|                                            |
 
---
-> Limit: 100 row(s)  (actual time=9498..9498 rows=100 loops=1)
    -> Sort: `sum(OS.quantity)` DESC, limit input to 100 row(s) per chunk  (actual time=9498..9498 rows=100 loops=1)
        -> Table scan on <temporary>  (actual time=9313..9444 rows=864341 loops=1)
            -> Aggregate using temporary table  (actual time=9313..9313 rows=864340 loops=1)
                -> Nested loop inner join  (cost=578091 rows=606350) (actual time=0.182..3777 rows=2e+6 loops=1)
                    -> Filter: (OS.created_at >= TIMESTAMP'2020-01-01 00:00:00')  (cost=188476 rows=606350) (actual time=0.13..334 rows=2e+6 loops=1)
                        -> Table scan on OS  (cost=188476 rows=1.82e+6) (actual time=0.127..253 rows=2e+6 loops=1)
                    -> Single-row index lookup on P using PRIMARY (product_id=OS.product_id)  (cost=0.543 rows=1) (actual time=0.00163..0.00165 rows=1 loops=2e+6)
```

### [인덱스 적용 후] 실행 계획
- 복합 인덱스 (created_at, product_id, quantity) 설정 후 인덱스 레인지 스캔으로 바뀌었으나 9.498s --> 9.159s 로 크게 향상되지 않음
```
id|select_type|table|partitions|type  |possible_keys                                                  |key                            |key_len|ref                 |rows  |filtered|Extra                                                    |
--+-----------+-----+----------+------+---------------------------------------------------------------+-------------------------------+-------+--------------------+------+--------+---------------------------------------------------------+
 1|SIMPLE     |OS   |          |range |order_statistics_product_id_IDX,order_statistics_created_at_IDX|order_statistics_created_at_IDX|8      |                    |909616|   100.0|Using where; Using index; Using temporary; Using filesort|
 1|SIMPLE     |P    |          |eq_ref|PRIMARY                                                        |PRIMARY                        |8      |hhplus.OS.product_id|     1|   100.0|                                                         |

---
-> Limit: 100 row(s)  (actual time=9212..9212 rows=100 loops=1)
    -> Sort: `sum(OS.quantity)` DESC, limit input to 100 row(s) per chunk  (actual time=9212..9212 rows=100 loops=1)
        -> Table scan on <temporary>  (actual time=9029..9159 rows=864341 loops=1)
            -> Aggregate using temporary table  (actual time=9029..9029 rows=864340 loops=1)
                -> Nested loop inner join  (cost=594607 rows=909616) (actual time=0.079..3623 rows=2e+6 loops=1)
                    -> Filter: (OS.created_at >= TIMESTAMP'2020-01-01 00:00:00')  (cost=185029 rows=909616) (actual time=0.0562..351 rows=2e+6 loops=1)
                        -> Covering index range scan on OS using order_statistics_created_at_IDX over ('2020-01-01 00:00:00.000000' <= created_at)  (cost=185029 rows=909616) (actual time=0.053..276 rows=2e+6 loops=1)
                    -> Single-row index lookup on P using PRIMARY (product_id=OS.product_id)  (cost=0.35 rows=1) (actual time=0.00155..0.00156 rows=1 loops=2e+6)
```

### [SQL 튜닝] 조인 제거
- Product 테이블과의 조인을 제거하고 필요한 product_id 값을 가져와 다시 Product 테이블에 Query 하는 식으로 변경 

- 약 9.498s --> 2.9s 초로 단축

- MySQL 에서 조인은 Nested Loop 방식으로 이뤄지고 상품 테이블 로우가 약 100만건으로 매우 리소스를 많이 사용하는 것으로 추정 (?)
```mysql
SELECT
    product_id
FROM
    ORDER_STATISTICS
WHERE
    created_at >= '2020-01-01 00:00:00'
GROUP BY
    product_id
ORDER BY
    SUM(quantity) DESC
LIMIT
    100;
```
```
id|select_type|table|partitions|type |possible_keys                                                  |key                            |key_len|ref|rows  |filtered|Extra                                                    |
--+-----------+-----+----------+-----+---------------------------------------------------------------+-------------------------------+-------+---+------+--------+---------------------------------------------------------+
 1|SIMPLE     |OS   |          |range|order_statistics_product_id_IDX,order_statistics_created_at_IDX|order_statistics_created_at_IDX|8      |   |909616|   100.0|Using where; Using index; Using temporary; Using filesort|

---
-> Limit: 100 row(s)  (actual time=2912..2912 rows=100 loops=1)
    -> Sort: `sum(OS.quantity)` DESC, limit input to 100 row(s) per chunk  (actual time=2912..2912 rows=100 loops=1)
        -> Table scan on <temporary>  (actual time=2796..2866 rows=864341 loops=1)
            -> Aggregate using temporary table  (actual time=2796..2796 rows=864340 loops=1)
                -> Filter: (OS.created_at >= TIMESTAMP'2020-01-01 00:00:00')  (cost=185029 rows=909616) (actual time=0.0362..330 rows=2e+6 loops=1)
                    -> Covering index range scan on OS using order_statistics_created_at_IDX over ('2020-01-01 00:00:00.000000' <= created_at)  (cost=185029 rows=909616) (actual time=0.034..259 rows=2e+6 loops=1)

```

## 결론

- 불필요한 조인은 되도록 제거하도록 하자!
  - 조인할 때, 드리븐 테이블과 드라이빙 테이블을 생각해보자! 
  - 한방 쿼리보다 여러 개의 쿼리로 나눠서 날려보자!

- 유저 경험을 최대한 높이기 위해서는 250ms ~ 750ms 사이 내에 조회되어야 할 것 같다...
  - Materialized View 를 적용해보자!