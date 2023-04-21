package vbn.state.helpers;

import lombok.NonNull;
import vbn.state.constraints.*;
import vbn.state.value.AbstractConstant;
import vbn.state.value.AbstractSymbol;
import vbn.state.value.Value;

import javax.annotation.Nullable;
import java.util.Stack;

public class ComputeConstraints {

    @NonNull private final Stack<Value> valueStack = new Stack<>();

    private IOperand operand = null;

    private final GenerateConstraintVisitor opVisitor = new GenerateConstraintVisitor();

    /**
     * Add a symbol to be later used for constraints.
     * Left goes first.
     * @param symName the name of the symbol
     */
    public void pushSymbol(@NonNull AbstractSymbol symName) {
        valueStack.push(symName);
    }

    /**
     * Add a constant to be later used for constraints.
     * Left goes first.
     * @param constant the constant object
     */
    public void pushConstant(@NonNull AbstractConstant constant) {
        valueStack.push(constant);
    }

    public void setOperand(@NonNull IOperand operand) {
        this.operand = operand;
    }

    /**
     * Clear the Compute Constraints after computing constraints
     */
    private void clear() {
        valueStack.clear();
        operand = null;
    }

    /**
     * Generate constraints based on the calls
     * @param assignmentSymName The symbol name to assign the constraints to.
     */
    public AbstractConstraint generateFromPushes(@Nullable final AbstractSymbol assignmentSymName) {
        AbstractConstraint resultingConstraint;

        try {
            if (operand == null) {
                throw new MissingOperandException("The apply operator must be applied unless it is a reassignment");
            }

            opVisitor.assignmentSymName = assignmentSymName;
            opVisitor.valueStack = valueStack;
            operand.accept(opVisitor);

            resultingConstraint = opVisitor.getGeneratedConstraint();
            clear();

        } catch (MissingOperandException e) {
            throw new RuntimeException(e);
        }
        return resultingConstraint;
    }

    public AbstractConstraint generateFromPushes(int lineNumber, @Nullable final AbstractSymbol assignmentSymName) {
        var constraint = generateFromPushes(assignmentSymName);
        constraint.setLineNumber(lineNumber);
        return constraint;
    }

    /**
     * Generate constraints based on the calls
     *
     * @return the newly created constraint
     */
    public AbstractConstraint generateFromPushes() {
        return generateFromPushes(null);
    }

    public boolean isReassignment() {
        return valueStack.size() == 1 && operand == null;
    }

    static class GenerateConstraintVisitor implements IOperandVisitor {
        private AbstractConstraint generatedConstraint;
        public AbstractSymbol assignmentSymName;
        public Stack<Value> valueStack;

        @SuppressWarnings("unused")
        public void visit(BinaryOperand binOp) {
            var right = valueStack.pop();
            var left = valueStack.pop();
            if (assignmentSymName == null) {
                generatedConstraint = new BinaryConstraint(left, binOp, right);
            }
            else {
                generatedConstraint = new BinaryConstraint(left, binOp, right, assignmentSymName);
            }
        }

        @SuppressWarnings("unused")
        public void visit(UnaryOperand unOp) {
            var symbol = valueStack.pop();
            if (assignmentSymName == null) {
                generatedConstraint = new UnaryConstraint(unOp, symbol);
            }
            else {
                generatedConstraint = new UnaryConstraint(unOp, symbol, assignmentSymName);
            }
        }

        @SuppressWarnings("unused")
        public void visit(CustomOperand customOp) {
            System.out.println("Need to handle customOp's such as cast");
        }

        public void visit(IOperand op) {
            throw new RuntimeException("An operator is not handled");
        }

        public AbstractConstraint getGeneratedConstraint() {
            return generatedConstraint;
        }
    }
}