package net.dunotech.venus.system.service.exception;

public class DataExportException extends RuntimeException {

    private static final long serialVersionUID = -1656866772572616980L;

    public DataExportException() {
        super();
    }

    public DataExportException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataExportException(String message) {
        super(message);
    }
}
