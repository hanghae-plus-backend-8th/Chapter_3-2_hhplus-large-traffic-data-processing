package kr.hhplus.be.server.point;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberPoint {

    @Schema(description = "사용자 식별자", type = "integer", format = "int64", example = "1")
    private long userId;

    @Schema(description = "포인트", type = "integer", format = "int64", example = "5000")
    private long point;
}
