package kr.hhplus.be.server.interfaces.api.order;

import kr.hhplus.be.server.application.order.OrderCommand.OrderCaptureCommand;
import kr.hhplus.be.server.application.order.OrderResult.OrderCaptureResult;
import kr.hhplus.be.server.application.order.OrderService;
import kr.hhplus.be.server.interfaces.api.order.OrderRequest.OrderCaptureRequest;
import kr.hhplus.be.server.interfaces.api.order.OrderResponse.OrderCaptureResponse;
import kr.hhplus.be.server.shared.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/order")
public class OrderController implements OrderControllerDocs {

    private final OrderService orderService;

    @PostMapping
    public CommonResponse<OrderCaptureResponse> capture(
            @Validated @ModelAttribute OrderCaptureRequest orderCaptureRequest
    ) {
        if (orderCaptureRequest.isNotValid()) {
            throw new IllegalArgumentException("상품 ID 목록과 수량 목록이 일치하지 않습니다.");
        }
        OrderCaptureResult orderCaptureResult = orderService.capture(new OrderCaptureCommand(orderCaptureRequest));
        OrderCaptureResponse orderCaptureResponse = new OrderCaptureResponse(orderCaptureResult);

        return CommonResponse.success(orderCaptureResponse);
    }
}
