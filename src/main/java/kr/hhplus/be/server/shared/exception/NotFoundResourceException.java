package kr.hhplus.be.server.shared.exception;

import lombok.Getter;

import static kr.hhplus.be.server.shared.constant.StatusCode.NO_DATA;

@Getter
public class NotFoundResourceException extends RuntimeException{

    private String code;

    public NotFoundResourceException() {
        super(NO_DATA.getMsg());
        this.code = NO_DATA.getCode();
    }

    public NotFoundResourceException(String message) {
        super(message);
        this.code = NO_DATA.getCode();
    }

    public NotFoundResourceException(String message, Throwable cause) {
        super(message, cause);
        this.code = NO_DATA.getCode();
    }
}
