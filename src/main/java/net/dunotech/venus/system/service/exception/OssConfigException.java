package net.dunotech.venus.system.service.exception;

public class OssConfigException extends RuntimeException {

    private static final long serialVersionUID = -1656866792573816981L;

    public OssConfigException() {
        super();
    }

    public OssConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public OssConfigException(String message) {
        super(message);
    }
}
