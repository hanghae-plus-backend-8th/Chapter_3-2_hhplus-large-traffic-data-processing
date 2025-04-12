package kr.hhplus.be.server.interfaces.api.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.product.ProductResponse.ProductInfoResponse;
import kr.hhplus.be.server.shared.dto.CommonResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "[상품]", description = "상품 관련 API 입니다.")
public interface ProductControllerDocs {

    @Operation(
            summary = "상품 조회 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "상품 조회 성공",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "code", schema = @Schema(type = "string", example = "SUCCESS", description = "코드")),
                                            @SchemaProperty(name = "data", schema = @Schema(implementation = ProductInfoResponse.class))
                                    }
                            )
                    )
            }
    )
    @ApiResponse(responseCode = "400", description = "유효하지 않은 상품 식별자", content = @Content)
    @ApiResponse(responseCode = "500", description = "서버 에러 발생", content = @Content)
    CommonResponse<ProductInfoResponse> info(
            @Parameter(description = "상품 식별자", example = "1") long productId
    );
}
