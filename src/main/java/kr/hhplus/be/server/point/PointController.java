package kr.hhplus.be.server.point;

import jakarta.validation.constraints.Min;
import kr.hhplus.be.server.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static kr.hhplus.be.server.common.constant.StatusCode.SUCCESS;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/point")
public class PointController implements PointControllerDocs {

    @GetMapping(path = "{userId}")
    public CommonResponse<MemberPoint> point(
            @Min(value = 1, message = "유효하지 않은 사용자 식별자입니다.")
            @PathVariable long userId
    ) {
        return new CommonResponse<>(SUCCESS.getCode(), null, new MemberPoint(userId, 5000L));
    }

    @GetMapping(path = "/{userId}/charge")
    public CommonResponse<MemberPoint> chargePoint(
            @Min(value = 1, message = "유효하지 않은 사용자 식별자입니다.")
            @PathVariable long userId,

            @Min(value = 1, message = "금액은 0보다 커야 합니다.")
            @RequestParam long amount
    ) {
        return new CommonResponse<>(SUCCESS.getCode(), null, new MemberPoint(userId, amount));
    }
}
