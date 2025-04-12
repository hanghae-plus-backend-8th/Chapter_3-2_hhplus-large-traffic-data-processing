package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.order.OrderRequest.OrderCaptureRequest;
import kr.hhplus.be.server.interfaces.api.order.OrderResponse.OrderCaptureResponse;
import kr.hhplus.be.server.shared.dto.CommonResponse;
import org.springdoc.core.annotations.ParameterObject;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "[주문]", description = "주문 관련 API 입니다.")
public interface OrderControllerDocs {

    @Operation(
            summary = "주문/결제 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주문/결제 성공",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "code", schema = @Schema(type = "string", example = "SUCCESS", description = "코드")),
                                            @SchemaProperty(name = "data", schema = @Schema(implementation = OrderCaptureResponse.class))
                                    }
                            )
                    )
            }
    )
    @ApiResponse(responseCode = "400", description = "유효하지 않은 상품 식별자", content = @Content)
    @ApiResponse(responseCode = "400 ", description = "유효하지 않은 사용자 식별자", content = @Content)
    @ApiResponse(responseCode = "400  ", description = "유효하지 않은 쿠폰번호", content = @Content)
    @ApiResponse(responseCode = "400   ", description = "포인트 잔고 부족", content = @Content)
    @ApiResponse(responseCode = "400    ", description = "유효기간이 만료된 쿠폰", content = @Content)
    @ApiResponse(responseCode = "400     ", description = "상품 재고 부족", content = @Content)
    @ApiResponse(responseCode = "500", description = "서버 에러 발생", content = @Content)
    CommonResponse<OrderCaptureResponse> capture(
            @ParameterObject OrderCaptureRequest orderCaptureRequest
    );
}
