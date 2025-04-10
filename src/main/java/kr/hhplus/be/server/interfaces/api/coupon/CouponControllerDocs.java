package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.coupon.CouponResponse.CouponDownloadResponse;
import kr.hhplus.be.server.shared.dto.CommonResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "[쿠폰]", description = "쿠폰 관련 API 입니다.")
public interface CouponControllerDocs {

    @Operation(
            summary = "쿠폰 발급 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "쿠폰 발급 성공",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "code", schema = @Schema(type = "string", example = "SUCCESS", description = "코드")),
                                            @SchemaProperty(name = "data", schema = @Schema(implementation = CouponDownloadResponse.class))
                                    }
                            )
                    )
            }
    )
    @ApiResponse(responseCode = "400", description = "유효하지 않은 사용자 식별자", content = @Content)
    @ApiResponse(responseCode = "400 ", description = "유효하지 않은 쿠폰 식별자", content = @Content)
    @ApiResponse(responseCode = "400  ", description = "쿠폰 잔여 수량 부족", content = @Content)
    @ApiResponse(responseCode = "400   ", description = "유효기간 만료된 쿠폰", content = @Content)
    @ApiResponse(responseCode = "500", description = "서버 에러 발생", content = @Content)
    CommonResponse<CouponDownloadResponse> download(
            @Parameter(description = "쿠폰 식별자", example = "1") long couponId,
            @Parameter(description = "사용자 식별자", example = "2") long memberId
    );
}
