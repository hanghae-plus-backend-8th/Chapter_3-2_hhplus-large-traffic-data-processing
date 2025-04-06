package kr.hhplus.be.server.common.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.be.server.common.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

import static kr.hhplus.be.server.common.constant.StatusCode.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@Order(value = 2)
@RestControllerAdvice
@RequiredArgsConstructor
public class ServletExceptionHandler {

    @ResponseStatus(code = UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public CommonResponse<String> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, HttpServletRequest request, HttpServletResponse response) {
        String currentMediaType = ex.getContentType() != null ? ex.getContentType().getType() + "/" + ex.getContentType().getSubtype() : "null";
        String supportedMediaTypes = ex.getSupportedMediaTypes().stream()
                .map(mediaType -> mediaType.getType() + "/" + mediaType.getSubtype())
                .collect(Collectors.joining(", "));
        String msg = "Current Content-Type : %s, Supported Content-Types : %s".formatted(currentMediaType, supportedMediaTypes);

        return new CommonResponse<>(NOT_SUPPORTED_MEDIA_TYPE.getCode(), msg, null);
    }

    @ResponseStatus(code = NOT_ACCEPTABLE)
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public CommonResponse<String> httpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex, HttpServletRequest request, HttpServletResponse response) {
        String currentMediaType = request.getHeader("Accept") != null ? request.getHeader("Accept") : "null";
        String supportedMediaTypes = ex.getSupportedMediaTypes().stream()
                .map(mediaType -> mediaType.getType() + "/" + mediaType.getSubtype())
                .collect(Collectors.joining(", "));
        String msg = "Current Accept-Type : %s, Supported Accept-Types : %s".formatted(currentMediaType, supportedMediaTypes);

        return new CommonResponse<>(NOT_ACCEPTABLE_TYPE.getCode(), msg, null);
    }

    @ResponseStatus(code = METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResponse<String> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request, HttpServletResponse response) {
        String currentMethod = ex.getMethod();
        String supportedMethods = ex.getSupportedHttpMethods() != null && !ex.getSupportedHttpMethods().isEmpty()
                ? ex.getSupportedHttpMethods()
                .stream()
                .map(HttpMethod::toString)
                .collect(Collectors.joining(", "))
                : "null";

        String msg = "Current Method : %s, Supported Methods : %s".formatted(currentMethod, supportedMethods);

        return new CommonResponse<>(NOT_SUPPORTED_METHOD.getCode(), msg, null);
    }

    @ResponseStatus(code = NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public CommonResponse<String> noHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request, HttpServletResponse response) {
        String msg = "%s %s : Not Found".formatted(ex.getHttpMethod(), request.getRequestURI());

        return new CommonResponse<>(NOT_FOUND_RESOURCE.getCode(), msg, null);
    }

    @ResponseStatus(code = NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public CommonResponse<String> noResourceFoundException(NoResourceFoundException ex, HttpServletRequest request, HttpServletResponse response) {
        String msg = "%s '%s' : Not Found".formatted(ex.getHttpMethod(), request.getRequestURI());

        return new CommonResponse<>(NOT_FOUND_RESOURCE.getCode(), msg, null);
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public CommonResponse<String> missingRequestHeaderException(MissingRequestHeaderException ex, HttpServletRequest request, HttpServletResponse response) {
        String msg = "Missing Http Header, Required header's Name : " + ex.getHeaderName();

        return new CommonResponse<>(REQ_HEADER.getCode(), msg, null);
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public CommonResponse<String> missingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response) {
        String msg = "Missing Required Parameter, Parameter's Name : " + ex.getParameterName();

        return new CommonResponse<>(REQ_PARAM.getCode(), msg, null);
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public CommonResponse<String> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request, HttpServletResponse response) {
        String paramName = ex.getName();
        String requiredParamType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : null;
        Object currentValue = ex.getValue();
        String msg = "Parameter's Type Mismatch Occurred. Parameter's type : %s, Parameter's name : %s, Current Value : %s".formatted(requiredParamType, paramName, currentValue);

        return new CommonResponse<>(BAD_PARAM.getCode(), msg, null);
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse<String> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response) {
        ObjectError error = ex.getAllErrors().get(0);
        String code = error.getCode();

        if (code != null && code.equals("typeMismatch")) {
            String msg = "Parameter's Type Mismatch Occurred.";

            if (error instanceof FieldError fieldError) {
                String fieldName = fieldError.getField();
                String fieldType = getFieldType(ex.getTarget(), fieldName);
                String currentValue = fieldError.getRejectedValue() != null ? fieldError.getRejectedValue().toString() : null;

                msg = "Parameter's Type Mismatch Occurred. Parameter's Type : %s, Parameter's name : %s, Current Value : %s".formatted(fieldType, fieldName, currentValue);
            }
            return new CommonResponse<>(BAD_PARAM.getCode(), msg, null);
        }
        return new CommonResponse<>(BAD_PARAM.getCode(), error.getDefaultMessage(), null);
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CommonResponse<String> HttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request, HttpServletResponse response) {
        return new CommonResponse<>(BAD_PARAM.getCode(), BAD_PARAM.getMsg(), null);
    }

    private String getFieldType(Object target, String fieldName) {
        if (target == null || fieldName == null) {
            return null;
        }
        Class<?> targetClass = target.getClass();

        try {
            return targetClass.getDeclaredField(fieldName)
                    .getType()
                    .getSimpleName();
        } catch (NoSuchFieldException ex) {
            return null;
        }
    }
}
