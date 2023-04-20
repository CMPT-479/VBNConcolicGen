package vbn.state.constraints;

import vbn.state.helpers.ComputeConstraints;

public interface IOperand {
    public void accept(IOperandVisitor visitor);
}
