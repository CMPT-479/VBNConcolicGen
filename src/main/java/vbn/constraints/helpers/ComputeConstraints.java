package vbn.constraints.helpers;

import vbn.constraints.BinaryOperand;
import vbn.constraints.State;
import vbn.constraints.SymbolMissingException;
import vbn.constraints.UnaryOperand;

import java.util.Stack;

public class ComputeConstraints {

    private Stack<String> symbols = new Stack<>();

    private Object operand = null;

    /**
     * Add a symbol to be later used for constraints.
     * Left goes first.
     * @param symName the name of the symbol
     */
    public void pushSymbol(String symName) {
        symbols.push(symName);
    }
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
     * @param globalState the global state to store the newly generated constraints
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

                    String right = symbols.pop();
                    String left = symbols.pop();
                    if (assignmentSymName == null) {
                        globalState.pushConstraint(left, (BinaryOperand) operand, right);
                    }
                    else {
                        globalState.pushConstraint(left, (BinaryOperand) operand, right, assignmentSymName);
                    }
                    break;

                case 1:
                    // A unary operator
                    if (!(operand instanceof UnaryOperand)) {
                        throw new ComputeConstraintsException("The operand is not unary when there is one symbol to be operated on");
                    }

                    String symbol = symbols.pop();
                    if (assignmentSymName == null) {
                        globalState.pushConstraint((UnaryOperand) operand, symbol);
                    }
                    else {
                        globalState.pushConstraint((UnaryOperand) operand, symbol, assignmentSymName);
                    }
                    break;

                default:
                    throw new ComputeConstraintsException("Too many symbols have been pushed on to the stack.");
            }
        } catch (SymbolMissingException | ComputeConstraintsException e) {
            throw new RuntimeException(e);
        }

        clear();
    }

    /**
     * Generate constraints based on the calls
     * @param globalState the global state to store the newly generated constraints
     */
    public void generateConstraint(State globalState) {
        generateConstraint(globalState, null);
    }
}