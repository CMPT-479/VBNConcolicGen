package vbn.state.helpers;

import lombok.NonNull;
import vbn.state.constraints.*;
import vbn.state.value.AbstractConstant;
import vbn.state.value.Symbol;
import vbn.state.value.Value;

import javax.annotation.Nullable;
import java.util.Stack;

public class ComputeConstraints {

    @NonNull private final Stack<Value> symbols = new Stack<>();

    private Object operand = null;

    /**
     * Add a symbol to be later used for constraints.
     * Left goes first.
     * @param symName the name of the symbol
     */
    public void pushSymbol(@NonNull Symbol symName) {
        symbols.push(symName);
    }

    /**
     * Add a constant to be later used for constraints.
     * Left goes first.
     * @param constant the constant object
     */
    public void pushConstant(@NonNull AbstractConstant constant) {
        symbols.push(constant);
    }

    public void setOperand(@NonNull Object operand) {
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
     * @param assignmentSymName The symbol name to assign the constraints to.
     */
    public AbstractConstraint generateFromPushes(@Nullable final Symbol assignmentSymName) {
        var numOfOps = symbols.size();
        AbstractConstraint result;

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
                        result = new BinaryConstraint(left, (BinaryOperand) operand, right);
                    }
                    else {
                        result = new BinaryConstraint(left, (BinaryOperand) operand, right, assignmentSymName);
                    }
                    break;

                case 1:
                    // A unary operator
                    if (!(operand instanceof UnaryOperand)) {
                        throw new ComputeConstraintsException("The operand is not unary when there is one symbol to be operated on");
                    }

                    var symbol = symbols.pop();
                    if (assignmentSymName == null) {
                        result = new UnaryConstraint((UnaryOperand) operand, symbol);
                    }
                    else {
                        result = new UnaryConstraint((UnaryOperand) operand, symbol, assignmentSymName);
                    }
                    break;

                default:
                    throw new ComputeConstraintsException("Too many symbols have been pushed on to the stack.");
            }
        } catch (ComputeConstraintsException e) {
            throw new RuntimeException(e);
        }

        clear();
        return result;
    }

    /**
     * Generate constraints based on the calls
     *
     * @return the newly created constraint
     */
    public AbstractConstraint generateFromPushes() {
        return generateFromPushes(null);
    }
}