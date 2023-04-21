package vbn.state.helpers;

public class MissingOperandException extends ComputeException {
    private static final long serialVersionUID = 0L;

    public MissingOperandException(String errorMessage) {
        super(errorMessage);
    }
}
