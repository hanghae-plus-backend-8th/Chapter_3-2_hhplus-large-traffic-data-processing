package kr.hhplus.be.server.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.hhplus.be.server.shared.constant.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static kr.hhplus.be.server.shared.constant.StatusCode.SUCCESS;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "msg")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String msg;

    @JsonProperty(value = "data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(SUCCESS.getCode(), null, data);
    }

    public static <T> CommonResponse<T> success(String msg) {
        return new CommonResponse<>(SUCCESS.getCode(), msg, null);
    }

    public static <T> CommonResponse<T> error(StatusCode errorCode) {
        return new CommonResponse<>(errorCode.getCode(), errorCode.getMsg(), null);
    }

    public static <T> CommonResponse<T> error(StatusCode errorCode, String msg) {
        return new CommonResponse<>(errorCode.getCode(), msg, null);
    }
}
