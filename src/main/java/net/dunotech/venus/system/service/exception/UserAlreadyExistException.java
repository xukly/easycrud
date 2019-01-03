package net.dunotech.venus.system.service.exception;

public class UserAlreadyExistException extends RuntimeException{

    private static final long serialVersionUID = -1656866792575836981L;

    public UserAlreadyExistException() {
        super();
    }

    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
