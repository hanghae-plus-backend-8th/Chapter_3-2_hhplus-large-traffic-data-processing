package kr.hhplus.be.server.product;

import jakarta.validation.constraints.Min;
import kr.hhplus.be.server.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static kr.hhplus.be.server.common.constant.StatusCode.SUCCESS;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/product")
public class ProductController implements ProductControllerDocs {

    @GetMapping(path = "{productId}")
    public CommonResponse<ProductResponse> info(
            @Min(value = 1, message = "유효하지 않은 상품 식별자입니다.")
            @PathVariable long productId
    ) {
        return new CommonResponse<>(SUCCESS.getCode(), null, new ProductResponse(productId, "에어맥스", 10000L, 5));
    }

    @GetMapping(path = "top")
    public CommonResponse<List<TopProductResponse>> topProducts() {
        List<TopProductResponse> topProducts = new ArrayList<>();
        topProducts.add(new TopProductResponse(1, 10, "에어맥스", 5000L, 10));
        topProducts.add(new TopProductResponse(2, 11, "나이키", 2000L, 20));
        topProducts.add(new TopProductResponse(3, 12, "뉴발란스", 10000L, 50));
        topProducts.add(new TopProductResponse(4, 13, "퓨마", 7000L, 5));
        topProducts.add(new TopProductResponse(5, 14, "삼선 슬리퍼", 3000L, 100));

        return new CommonResponse<>(SUCCESS.getCode(), null, topProducts);
    }
}
