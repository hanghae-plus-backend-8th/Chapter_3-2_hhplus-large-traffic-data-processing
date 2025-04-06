package kr.hhplus.be.server.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
}
