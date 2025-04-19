package kr.hhplus.be.server.infrastructure.product.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static kr.hhplus.be.server.infrastructure.order.persistence.QOrderStatisticsEntity.orderStatisticsEntity;
import static kr.hhplus.be.server.infrastructure.product.persistence.QProductEntity.productEntity;

@RequiredArgsConstructor
public class ProductJpaRepositoryCustomImpl implements ProductJpaRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<ProductEntity> findTopProducts(int limit) {
        return query
                .select(productEntity)
                .from(orderStatisticsEntity)
                .innerJoin(productEntity)
                .on(
                        orderStatisticsEntity.productId.eq(productEntity.productId)
                )
                .where(orderStatisticsEntity.createdAt.goe(LocalDate.now().atStartOfDay()))
                .groupBy(orderStatisticsEntity.productId)
                .orderBy(orderStatisticsEntity.quantity.sum().desc())
                .limit(limit)
                .fetch();
    }
}
