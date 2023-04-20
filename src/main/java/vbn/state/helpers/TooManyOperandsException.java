package vbn.state.helpers;

public class TooManyOperandsException extends Exception {
    private static final long serialVersionUID = 0L;

    public TooManyOperandsException(String errorMessage) {
        super(errorMessage);
    }
}
