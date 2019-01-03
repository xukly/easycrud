package net.dunotech.venus.system.service.exception;

public class DataDeleteFailedException extends RuntimeException {
    private static final long serialVersionUID = -1656866792572616980L;

    public DataDeleteFailedException() {
        super();
    }

    public DataDeleteFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataDeleteFailedException(String message) {
        super(message);
    }
}
