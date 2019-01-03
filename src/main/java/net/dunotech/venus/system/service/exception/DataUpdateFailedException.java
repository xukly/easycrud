package net.dunotech.venus.system.service.exception;

public class DataUpdateFailedException extends RuntimeException {
    private static final long serialVersionUID = -1656866792552616981L;

    public DataUpdateFailedException() {
        super();
    }

    public DataUpdateFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataUpdateFailedException(String message) {
        super(message);
    }
}
