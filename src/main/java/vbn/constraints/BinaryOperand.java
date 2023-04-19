package vbn.constraints;

/**
 * Note: We could separate these into different enums, but decided the additional complexity was not worth it
 */
public enum BinaryOperand implements IOperand {
    // Logical
    AND,
    OR,
    EQ,

    // Boolean Arithmetic
    // TODO: Implement LOG_AND, LOG_OR, LOG_NOR, etc.

    // Arithmetic
    ADD,
    MINUS,
    MULTIPLY,
    DIVIDE,
    POWER,

    // Integer Comparison
    INT_LT, // less than
    INT_LTE, // less than or equal
    INT_EQ,
    INT_NEQ,
    INT_GT,
    INT_GTE,
}
