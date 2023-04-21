package vbn.state.constraints;

import vbn.state.helpers.ComputeConstraints;

import java.io.Serializable;

public enum CustomOperand implements IOperand {
    CAST, // Cast the operand to another type
    REASSIGN, // Set the value of one operand to another

    ;

    @Override
    public void accept(ComputeConstraints.GenerateConstraintVisitor visitor) {
        visitor.visit(this);
    }
}
