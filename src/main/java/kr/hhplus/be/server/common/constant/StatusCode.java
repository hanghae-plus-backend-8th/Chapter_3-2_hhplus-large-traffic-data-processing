package kr.hhplus.be.server.common.constant;

import lombok.Getter;

@Getter
public enum StatusCode {

    // Status - 2xx

        // 200
        SUCCESS("SUCCESS", "요청 성공"),

    // Status - 3xx

    // Status - 4xx

        // 400
            // General
            REQ_PARAM( "REQ_PARAM", "필수 파라미터가 누락되었습니다."),
            BAD_PARAM("BAD_PARAM", "잘못된 파라미터입니다."),
            REQ_HEADER("REQ_HEADER", "HTTP 헤더가 누락되었습니다."),
            NO_DATA("NO_DATA","조회되는 데이터가 없습니다."),

        // 401
        NO_AUTHENTICATION("NO_AUTHENTICATION", "인증이 필요합니다."),

        // 403
        NO_PERMISSION("NO_PERMISSION", "접근 권한이 없습니다."),

        // 404
        NOT_FOUND_RESOURCE("NOT_FOUND_RESOURCE", "요청에 해당하는 Resource를 찾을 수 없습니다."),

        // 405
        NOT_SUPPORTED_METHOD("NOT_SUPPORTED_METHOD", "지원되지 않는 HTTP 메소드입니다."),

        // 406
        NOT_ACCEPTABLE_TYPE("NOT_ACCEPTABLE_TYPE", "지원되지 않는 Accept-Type 입니다."),

        // 409
        DUPLE_DATA("DUPLE_DATA", "중복 데이터입니다."),

        // 415
        NOT_SUPPORTED_MEDIA_TYPE("NOT_SUPPORTED_MEDIA_TYPE", "지원되지 않는 Content-Type 입니다."),

    // Status - 5xx

        // 500
        SERVER_ERROR("SERVER_ERROR", "예기치 못한 오류가 발생하였습니다."),
    ;
    private final String code;
    private final String msg;

    StatusCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
