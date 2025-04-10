package kr.hhplus.be.server.interfaces.api.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.point.PointResponse.PointChargeResponse;
import kr.hhplus.be.server.shared.dto.CommonResponse;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = "[포인트]", description = "포인트 관련 API 입니다.")
public interface PointControllerDocs {

    @Operation(
            summary = "포인트 충전 API",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "포인트 충전 성공",
                            content = @Content(
                                    mediaType = APPLICATION_JSON_VALUE,
                                    schemaProperties = {
                                            @SchemaProperty(name = "code", schema = @Schema(type = "string", example = "SUCCESS", description = "코드")),
                                            @SchemaProperty(name = "data", schema = @Schema(implementation = PointChargeResponse.class))
                                    }
                            )
                    )
            }
    )
    @ApiResponse(responseCode = "400", description = "유효하지 않은 사용자 식별자", content = @Content)
    @ApiResponse(responseCode = "500", description = "서버 에러 발생", content = @Content)
    CommonResponse<PointChargeResponse> charge(
            @Parameter(description = "사용자 식별자", example = "1") long memberId,
            @Parameter(description = "금액", example = "1000") long amount
    );
}
