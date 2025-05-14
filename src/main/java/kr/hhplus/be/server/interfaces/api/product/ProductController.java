package kr.hhplus.be.server.interfaces.api.product;

import kr.hhplus.be.server.application.product.ProductResult.ProductInfoResult;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.interfaces.api.product.ProductResponse.ProductInfoResponse;
import kr.hhplus.be.server.interfaces.api.product.ProductResponse.ProductPopularResponse;
import kr.hhplus.be.server.shared.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/products")
public class ProductController implements ProductControllerDocs {

    private final ProductService productService;

    @GetMapping(path = "{productId}")
    public CommonResponse<ProductInfoResponse> info(
            @PathVariable long productId
    ) {
        ProductInfoResult productInfoResult = productService.info(productId);
        ProductInfoResponse productInfoResponse = new ProductInfoResponse(productInfoResult);

        return CommonResponse.success(productInfoResponse);
    }

    @GetMapping(path = "popular")
    public CommonResponse<List<ProductPopularResponse>> popular(
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<ProductPopularResponse> topProducts = productService.popular(limit)
                .stream()
                .map(ProductPopularResponse::new)
                .toList();

        return CommonResponse.success(topProducts);
    }
}
