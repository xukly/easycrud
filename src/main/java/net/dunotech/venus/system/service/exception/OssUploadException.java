package net.dunotech.venus.system.service.exception;

public class OssUploadException extends RuntimeException {

    private static final long serialVersionUID = -1656866792573836981L;

    public OssUploadException() {
        super();
    }

    public OssUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public OssUploadException(String message) {
        super(message);
    }
}
