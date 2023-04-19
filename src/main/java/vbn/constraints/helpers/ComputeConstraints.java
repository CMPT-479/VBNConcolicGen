package vbn.constraints.helpers;

import vbn.constraints.*;

import java.util.Stack;

public class ComputeConstraints {

    private final Stack<Object> symbols = new Stack<>();

    private Object operand = null;

    /**
     * Add a symbol to be later used for constraints.
     * Left goes first.
     *
     * @param constant the value of the constant
     */
    public void pushConstant(Object constant) {
        symbols.push(constant);
    }

    /**
     * Add a symbol to be later used for constraints.
     * Left goes first.
     *
     * @param symName the name of the symbol
     */
    public void pushSymbol(String symName) throws TooManyOperandsException {
        if (symbols.size() == 2) {
            throw new TooManyOperandsException("Too many symbols have been pushed on to the stack.");
        }

        symbols.push(symName);
    }

    /**
     * Set the operand
     *
     * @param operand the operand to apply to the (max two symbols)
     */
    public void setOperand(Object operand) {
        this.operand = operand;
    }

    /**
     * Clear the Compute Constraints after computing constraints
     */
    private void clear() {
        symbols.clear();
        operand = null;
    }

    /**
     * Generate constraints based on the calls
     *
     * @param globalState       the global state to store the newly generated constraints
     * @param assignmentSymName The symbol name to assign the constraints to.
     */
    public void generateConstraint(State globalState, final String assignmentSymName) {
        var numOfOps = symbols.size();

        try { // FIXME: Is this good practice?
            if (operand == null) {
                throw new ComputeConstraintsException("The temp operand is null when it should be filled when there > 1 operands");
            }

            switch (numOfOps) {
                case 2:
                    if (!(operand instanceof BinaryOperand)) {
                        throw new ComputeConstraintsException("The operand is not binary when there are two symbols to be operated on");
                    }

                    var right = symbols.pop();
                    var left = symbols.pop();

                    if (assignmentSymName == null) {
                        if (left instanceof String && right instanceof String) {
                            globalState.pushConstraint(
                                    (String) left,
                                    (BinaryOperand) operand,
                                    (String) right
                            );
                        } else if (left instanceof String) {
                            globalState.pushConstraint(new BinaryConstraint<>(
                                    (String) left,
                                    (BinaryOperand) operand,
                                    right)
                            );
                        } else if (right instanceof String) {
                            globalState.pushConstraint(new BinaryConstraint<>(
                                    left,
                                    (BinaryOperand) operand,
                                    (String) right)
                            );
                        } else {
                            throw new ComputeConstraintsException("Both operands are nonStrings");
                        }
                    } else {
                        if (left instanceof String && right instanceof String) {
                            globalState.pushConstraint(
                                    (String) left,
                                    (BinaryOperand) operand,
                                    (String) right,
                                    assignmentSymName
                            );
                        } else if (left instanceof String) {
                            globalState.pushConstraint(new BinaryConstraint<>(
                                    (String) left,
                                    (BinaryOperand) operand,
                                    right,
                                    globalState.getSymbol(assignmentSymName)));
                        } else if (right instanceof String) {
                            globalState.pushConstraint(new BinaryConstraint<>(
                                    left,
                                    (BinaryOperand) operand,
                                    (String) right,
                                    globalState.getSymbol(assignmentSymName)));
                        } else {
                            throw new ComputeConstraintsException("Both operands are nonStrings");
                        }
                    }
                    break;

                case 1:
                    // A unary operator
                    if (!(operand instanceof UnaryOperand)) {
                        throw new ComputeConstraintsException("The operand is not unary when there is one symbol to be operated on");
                    }

                    var symbol = symbols.pop();

                    if (!(symbol instanceof String)) {
                        throw new ComputeConstraintsException("The symbol is a constant when it must be a symbol");
                    }

                    if (assignmentSymName == null) {
                        globalState.pushConstraint((UnaryOperand) operand, (String) symbol);
                    } else {
                        globalState.pushConstraint((UnaryOperand) operand, (String) symbol, assignmentSymName);
                    }
                    break;

                default:
                    throw new TooManyOperandsException("Too many symbols have been pushed on to the stack.");
            }
        } catch (SymbolMissingException | ComputeConstraintsException e) {
            throw new RuntimeException(e);
        }

        clear();
    }

    /**
     * Generate constraints based on the calls
     *
     * @param globalState the global state to store the newly generated constraints
     */
    public void generateConstraint(State globalState) {
        generateConstraint(globalState, null);
    }
}