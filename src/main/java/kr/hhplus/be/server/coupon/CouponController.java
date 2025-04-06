package kr.hhplus.be.server.coupon;

import jakarta.validation.constraints.Min;
import kr.hhplus.be.server.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static kr.hhplus.be.server.common.constant.StatusCode.SUCCESS;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/coupon")
public class CouponController implements CouponControllerDocs {

    @PostMapping(path = "{couponId}/download")
    public CommonResponse<CouponResponse> giveCoupon(
            @Min(value = 1, message = "유효하지 않은 쿠폰 식별자입니다.")
            @PathVariable long couponId,

            @Min(value = 1, message = "유효하지 않은 사용자 식별자입니다.")
            @RequestParam long userId
    ) {
        return new CommonResponse<>(SUCCESS.getCode(), null, new CouponResponse(userId, couponId, UUID.randomUUID().toString().replace("-", "").substring(0, 20)));
    }
}
