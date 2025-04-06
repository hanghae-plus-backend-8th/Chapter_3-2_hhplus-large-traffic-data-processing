package kr.hhplus.be.server.order;

import kr.hhplus.be.server.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kr.hhplus.be.server.common.constant.StatusCode.SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/order")
public class OrderController implements OrderControllerDocs {

    @PostMapping
    public CommonResponse<OrderResponse> order(
            @Validated @RequestBody OrderCreate orderCreate
    ) {
        return new CommonResponse<>(SUCCESS.getCode(), null, new OrderResponse(1L, 2L, 3000L, 2000L, 1000L));
    }
}
