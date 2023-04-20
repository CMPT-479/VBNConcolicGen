package vbn.state.constraints;

import java.io.Serializable;

public enum UnaryOperand implements IOperand, Serializable {
    NOT, // Jimple does not have NOT statement (it is handled through conditionals)
    NEG,
}
