package vbn.state.constraints;

import vbn.state.helpers.ComputeConstraints;

public enum UnaryOperand implements IOperand {
    NOT, // Jimple does not have NOT statement (it is handled through conditionals)
    NEG,
    ;

    @Override
    public void accept(ComputeConstraints.GenerateConstraintVisitor visitor) {
        visitor.visit(this);
    }
}
