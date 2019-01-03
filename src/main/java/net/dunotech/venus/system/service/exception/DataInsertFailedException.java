package net.dunotech.venus.system.service.exception;

public class DataInsertFailedException extends RuntimeException {
    private static final long serialVersionUID = -1656866792572616981L;

    public DataInsertFailedException() {
        super();
    }

    public DataInsertFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataInsertFailedException(String message) {
        super(message);
    }
}
