package vbn.state.helpers;

public class MissingAssignmentSymbolException extends ComputeException {
    private static final long serialVersionUID = 0L;

    public MissingAssignmentSymbolException(String errorMessage) {
        super(errorMessage);
    }
}
