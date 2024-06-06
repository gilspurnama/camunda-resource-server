package com.wistkey.md.util;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

public enum ExceptionEnum {
    SUCCESS                                                 (200, HttpStatus.OK, null),
    USER_NOT_FOUND                                          (40001, HttpStatus.NOT_FOUND, "User Not Found"),
    USER_NOT_ASSIGN_TO_ROLE                                 (40002, HttpStatus.NOT_FOUND, "User Not Assign to Any Role"),
    USER_NOT_ADMIN_UNAUTHORIZED                             (40003, HttpStatus.UNAUTHORIZED, "User's Role Is Not Authorized For This Action"),
    USER_UNKNOWN_ERROR                                      (40999, HttpStatus.BAD_REQUEST, "User Unknown Error"),
    ROLE_NOT_FOUND                                          (41001, HttpStatus.NOT_FOUND, "Role Not Found"),
    ROLE_UNKNOWN_ERROR                                      (41999, HttpStatus.BAD_REQUEST, "Role Unknown Error"),
    USER_ROLE_NOT_FOUND                                     (42001, HttpStatus.NOT_FOUND, "User Role Not Found"),
    USER_ROLE_UNKNOWN_ERROR                                 (42999, HttpStatus.BAD_REQUEST, "User Role Unknown Error"),
    PERMISSION_NOT_FOUND                                    (43001, HttpStatus.NOT_FOUND, "Permission Not Found"),
    PERMISSION_UNKNOWN_ERROR                                (43999, HttpStatus.BAD_REQUEST, "Permission Unknown Error"),
    ROLE_PERMISSION_NOT_FOUND                               (44001, HttpStatus.NOT_FOUND, "Role Permission Not Found"),
    ROLE_PERMISSION_UNKNOWN_ERROR                           (44999, HttpStatus.BAD_REQUEST, "Role Permission Unknown Error");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    ExceptionEnum(Integer code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public HttpStatus httpStatus() {
        return this.httpStatus;
    }

    public String message() {
        return this.message;
    }

    public static HttpStatus getHttpStatus(Integer code){
        for (ExceptionEnum item : ExceptionEnum.values()) {
            if (code.equals(item.code())) {
                return item.httpStatus;
            }
        }
        return HttpStatus.BAD_REQUEST;
    }

    public static String getMessage(Integer code, String defaultMessage) {
        for (ExceptionEnum item : ExceptionEnum.values()) {
            if (code.equals(item.code()) && !StringUtils.hasLength(item.message)) {
                return item.message;
            }
        }
        return defaultMessage;
    }

    public static String getMessage(ExceptionEnum exceptionEnum) {
        return getMessage(exceptionEnum, exceptionEnum.message());
    }

    public static String getMessage(ExceptionEnum exceptionEnum, String customErrorMessage) {
        return String.format("%d - %s", exceptionEnum.code(), customErrorMessage);
    }
}
