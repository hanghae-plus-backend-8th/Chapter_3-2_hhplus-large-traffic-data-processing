package kr.hhplus.be.server.shared.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.be.server.shared.dto.CommonResponse;
import kr.hhplus.be.server.shared.exception.NotFoundResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static kr.hhplus.be.server.shared.constant.StatusCode.NO_DATA;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Order(value = 2)
@RestControllerAdvice
@RequiredArgsConstructor
public class BusinessExceptionHandler {

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(NotFoundResourceException.class)
    public CommonResponse<String> notFoundResourceException(NotFoundResourceException ex, HttpServletRequest request, HttpServletResponse response) {
        return CommonResponse.error(NO_DATA, ex.getMessage());
    }
}
