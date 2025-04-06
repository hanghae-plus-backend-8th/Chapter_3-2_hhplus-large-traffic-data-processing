package kr.hhplus.be.server.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreate {

    @Schema(description = "사용자 식별자",
            type = "integer",
            format = "int64",
            example = "1")
    @Min(value = 1, message = "유효하지 않은 사용자 식별자입니다.")
    private long userId;

    @NotNull(message = "주문하신 상품이 없습니다.")
    @NotEmpty(message = "주문하신 상품이 없습니다.")
    private List<ProductInfo> orderProducts;

    @Getter
    @AllArgsConstructor
    public static class ProductInfo {
        @Schema(description = "상품 식별자",
                type = "integer",
                format = "int64",
                example = "2")
        private long productId;

        @Schema(description = "구매 수량",
                type = "integer",
                example = "3")
        private int quantity;
    }
}
