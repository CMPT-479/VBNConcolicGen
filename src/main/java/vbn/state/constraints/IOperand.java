package vbn.state.constraints;

import vbn.state.helpers.ComputeConstraints;

public interface IOperand{
    // How can I allow any class that implements IOperatorVisitor?
    public void accept(ComputeConstraints.GenerateConstraintVisitor visitor);
}
