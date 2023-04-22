package vbn.state.constraints;

import vbn.state.VBNLibraryRuntimeException;
import vbn.state.helpers.ComputeConstraints;
import vbn.state.helpers.IncorrectNumberOfValuesException;

/**
 * Note: We could separate these into different enums, but decided the additional complexity was not worth it
 */
public enum BinaryOperand implements IOperand {
    // Both Boolean and Numbers
    EQ,
    NEQ,

    // Boolean Operators
    AND,
    OR,


    // Boolean Arithmetic
    // Implement LOG_AND, LOG_OR, LOG_NOR, etc.

    // Arithmetic
    ADD,
    MINUS,
    MULTIPLY,
    DIVIDE,

    // POWER, // SOOT does not have power

    // Integer Comparison
    LT, // less than
    LTE, // less than or equal
    GT,
    GTE,

    // Floating Point Comparison
    // FIXME: We approximate floating point comparison with integer comparison
//    CMPL,
//    CMPG,
//    CMP, // Don't know how to handle

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
