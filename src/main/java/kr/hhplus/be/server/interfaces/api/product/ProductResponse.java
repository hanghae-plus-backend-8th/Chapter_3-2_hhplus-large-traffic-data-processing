package kr.hhplus.be.server.interfaces.api.product;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.product.ProductResult;
import kr.hhplus.be.server.application.product.ProductResult.ProductInfoResult;
import kr.hhplus.be.server.application.product.ProductResult.ProductPopularResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ProductResponse {

    @Getter
    @AllArgsConstructor
    public static class ProductInfoResponse {

        @Schema(description = "상품 ID", type = "integer", format = "int64", example = "1")
        private long productId;

        @Schema(description = "상품명", type = "string", example = "에어맥스")
        private String name;

        @Schema(description = "가격", type = "integer", format = "int64", example = "5000")
        private long price;

        @Schema(description = "수량", type = "integer", format = "int64", example = "10")
        private int quantity;

        public ProductInfoResponse(ProductInfoResult productInfoResult) {
            this.productId = productInfoResult.getProductId();
            this.name = productInfoResult.getName();
            this.price = productInfoResult.getPrice();
            this.quantity = productInfoResult.getQuantity();
        }
    }

    @Getter
    @AllArgsConstructor
    @ArraySchema(schema = @Schema(type = "array", description = "인기 상품 리스트 응답"))
    public static class ProductPopularResponse {
        @Schema(description = "상품 ID", type = "integer", format = "int64", example = "1")
        private long productId;

        @Schema(description = "상품명", type = "string", example = "에어맥스")
        private String name;

        @Schema(description = "가격", type = "integer", format = "int64", example = "5000")
        private long price;

        @Schema(description = "수량", type = "integer", format = "int64", example = "10")
        private int quantity;

        public ProductPopularResponse(ProductPopularResult productPopularResult) {
            this.productId = productPopularResult.getProductId();
            this.name = productPopularResult.getName();
            this.price = productPopularResult.getPrice();
            this.quantity = productPopularResult.getQuantity();
        }
    }
}
