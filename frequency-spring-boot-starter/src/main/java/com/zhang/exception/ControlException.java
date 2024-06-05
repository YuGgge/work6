package com.zhang.exception;

import org.springframework.stereotype.Component;

/**
 * 异常处理
 */
@Component
public class ControlException extends RuntimeException{
    /**错误码*/
    protected String code;
    public ControlException() {}

    public ControlException(Throwable ex) {
        super(ex);
    }
    public ControlException(String message) {
        super(message);
    }
    public ControlException(String code, String message) {
        super(message);
        this.code = code;
    }
    public ControlException(String message, Throwable ex) {
        super(message, ex);
    }
}
