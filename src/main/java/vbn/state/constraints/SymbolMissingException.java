package vbn.state.constraints;

public class SymbolMissingException extends Exception {
    private static final long serialVersionUID = 0L;

    public SymbolMissingException(String errorMessage) {
        super(errorMessage);
    }
}
