package vbn.state.constraints;

import vbn.state.helpers.ComputeConstraints;

public interface IOperand{
    // TODO: Need To Support IOperatorVisitor
    public void accept(ComputeConstraints.GenerateConstraintVisitor visitor);
}
