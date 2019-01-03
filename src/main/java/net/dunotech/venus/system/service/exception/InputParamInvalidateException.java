package net.dunotech.venus.system.service.exception;

public class InputParamInvalidateException extends RuntimeException {
    private static final long serialVersionUID = -1656866792572612580L;

    public InputParamInvalidateException() {
        super();
    }

    public InputParamInvalidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputParamInvalidateException(String message) {
        super(message);
    }
}
