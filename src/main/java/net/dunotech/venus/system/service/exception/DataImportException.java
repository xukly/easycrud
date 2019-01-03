package net.dunotech.venus.system.service.exception;

public class DataImportException extends RuntimeException {
    private static final long serialVersionUID = -1656866772571616980L;

    public DataImportException() {
        super();
    }

    public DataImportException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataImportException(String message) {
        super(message);
    }
}
