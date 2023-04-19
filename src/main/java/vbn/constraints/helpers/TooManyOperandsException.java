package vbn.constraints.helpers;

public class TooManyOperandsException extends ComputeConstraintsException {
    public TooManyOperandsException(String errorMessage) {
        super(errorMessage);
    }
}
