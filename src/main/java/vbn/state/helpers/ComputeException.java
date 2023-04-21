package vbn.state.helpers;

import vbn.state.IVBNException;

public class ComputeException extends Exception implements IVBNException {
    private static final long serialVersionUID = 0L;

    public ComputeException(String errorMessage) {
        super(errorMessage);
    }
}
