package vbn.state.constraints;

import vbn.state.helpers.ComputeConstraints;
import vbn.state.helpers.ComputeException;
import vbn.state.helpers.MissingAssignmentSymbolException;

import java.io.Serializable;

public enum CustomOperand implements IOperand {
    CAST, // Cast the operand to another type
    REASSIGN, // Set the value of one operand to another

    ;

    @Override
    public void accept(ComputeConstraints.GenerateConstraintVisitor visitor) {
        try {
            visitor.visit(this);
        } catch (ComputeException e) {
            throw new RuntimeException(e);
        }
    }
}
