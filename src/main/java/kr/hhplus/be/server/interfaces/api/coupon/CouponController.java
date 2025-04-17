package kr.hhplus.be.server.interfaces.api.coupon;

import kr.hhplus.be.server.application.coupon.CouponResult;
import kr.hhplus.be.server.application.coupon.CouponResult.CouponDownloadResult;
import kr.hhplus.be.server.application.coupon.CouponResult.CouponListResult;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.interfaces.api.coupon.CouponRequest.CouponListRequest;
import kr.hhplus.be.server.interfaces.api.coupon.CouponResponse.CouponDownloadResponse;
import kr.hhplus.be.server.interfaces.api.coupon.CouponResponse.CouponListResponse;
import kr.hhplus.be.server.shared.dto.CommonResponse;
import kr.hhplus.be.server.shared.dto.ListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/coupon")
public class CouponController implements CouponControllerDocs {

    private final CouponService couponService;

    @PostMapping(path = "{couponId}/download")
    public CommonResponse<CouponDownloadResponse> download(
            @PathVariable long couponId,
            @RequestParam long memberId
    ) {
        CouponDownloadResult couponDownloadResult = couponService.download(couponId, memberId);
        CouponDownloadResponse couponDownloadResponse = new CouponDownloadResponse(couponDownloadResult);

        return CommonResponse.success(couponDownloadResponse);
    }

    @GetMapping(path = "list")
    public CommonResponse<ListDto<CouponListResponse>> list(
            @Validated @ModelAttribute CouponListRequest couponListRequest
    ) {
        ListDto<CouponListResult> couponResultList = couponService.list(couponListRequest.toCommand());
        ListDto<CouponListResponse> couponResponseList = couponResultList.map(CouponListResponse::new);

        return CommonResponse.success(couponResponseList);
    }
}
