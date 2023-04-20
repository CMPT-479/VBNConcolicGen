package vbn.state.helpers;

public class MissingOperandException extends Exception {
    private static final long serialVersionUID = 0L;

    public MissingOperandException(String errorMessage) {
        super(errorMessage);
    }
}
