package vbn.state.constraints;

import vbn.state.VBNLibraryRuntimeException;
import vbn.state.helpers.ComputeConstraints;
import vbn.state.helpers.IncorrectNumberOfValuesException;

public enum UnaryOperand implements IOperand {
    NOT, // Jimple does not have a NOT statement (it is handled through conditionals)
    NEG,
    ;

    @Override
    public void accept(ComputeConstraints.GenerateConstraintVisitor visitor) {
        try {
            visitor.visit(this);
        } catch (IncorrectNumberOfValuesException e) {
            throw new VBNLibraryRuntimeException(e);
        }
    }
}
