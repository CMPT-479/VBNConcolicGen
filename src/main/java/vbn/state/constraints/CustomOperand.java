package vbn.state.constraints;

import vbn.state.helpers.ComputeConstraints;

public enum CustomOperand implements IOperand {
    CAST, // Cast the operand to another type
    REASSIGN, // Set the value of one operand to another

    ;

    @Override
    public void accept(IOperandVisitor visitor) {
        visitor.visit(this);
    }
}
