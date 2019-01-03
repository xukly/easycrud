package net.dunotech.venus.system.service.exception;

public class ArgumentNotValidException extends RuntimeException {
    private static final long serialVersionUID = -1656866792572610580L;

    public ArgumentNotValidException() {
        super();
    }

    public ArgumentNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentNotValidException(String message) {
        super(message);
    }

}
