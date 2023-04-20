package vbn.state.constraints;

public enum UnaryOperand implements IOperand {
    NOT, // Jimple does not have NOT statement (it is handled through conditionals)
    NEG,
}
