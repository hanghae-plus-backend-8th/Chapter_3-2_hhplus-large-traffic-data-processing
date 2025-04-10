package kr.hhplus.be.server.interfaces.api.product;

import kr.hhplus.be.server.application.product.ProductResult.ProductInfoResult;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.shared.dto.CommonResponse;
import kr.hhplus.be.server.interfaces.api.product.ProductResponse.ProductInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
