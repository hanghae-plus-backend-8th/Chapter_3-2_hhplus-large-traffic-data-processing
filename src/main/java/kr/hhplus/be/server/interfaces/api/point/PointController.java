package kr.hhplus.be.server.interfaces.api.point;

import kr.hhplus.be.server.application.point.PointResult.PointChargeResult;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.interfaces.api.point.PointResponse.PointChargeResponse;
import kr.hhplus.be.server.shared.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/point")
public class PointController implements PointControllerDocs {

    private final PointService pointService;

    @PatchMapping(path = "{memberId}/charge")
    public CommonResponse<PointChargeResponse> charge(
            @PathVariable long memberId,
            @RequestParam long amount
    ) {
        PointChargeResult pointChargeResult = pointService.charge(memberId, amount);
        PointChargeResponse pointChargeResponse = new PointChargeResponse(pointChargeResult);

        return CommonResponse.success(pointChargeResponse);
    }
}
