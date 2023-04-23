package vbn.state.helpers;

import lombok.NonNull;
import vbn.state.VBNLibraryRuntimeException;
import vbn.state.constraints.*;
import vbn.state.value.*;

import javax.annotation.Nullable;
import java.util.Stack;

public class ComputeConstraints {

    @NonNull private final Stack<Value> valueStack = new Stack<>();

    private IOperand operand = null;

    private final GenerateConstraintVisitor opVisitor = new GenerateConstraintVisitor();

    /**
     * The result the constraints evaluates to
     */
    private Boolean evaluatedResult;

    /**
     * Add a symbol to be later used for constraints.
     * Left goes first.
     * @param symName the name of the symbol
     */
    public void pushSymbol(@NonNull ISymbol symName) {
        valueStack.push(symName);
    }

    /**
     * Add a constant to be later used for constraints.
     * Left goes first.
     * @param constant the constant object
     */
    public void pushConstant(@NonNull IConstant constant) {
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
        evaluatedResult = null;
    }

    /**
     * Generate constraints based on the calls
     * @param assignmentSymName The symbol name to assign the constraints to.
     */
    private IConstraint generateFromPushes(int lineNumber, @Nullable final ISymbol assignmentSymName) {
        IConstraint resultingConstraint;

        if (operand == null) {
            throw new VBNLibraryRuntimeException("The operator must be set before generating constraint from pushes");
        }

        opVisitor.setValues(valueStack, isEvaluatedTrue(), lineNumber, assignmentSymName);
        operand.accept(opVisitor);

        // Values are copied in set values
        valueStack.clear();

        resultingConstraint = opVisitor.getGeneratedConstraint();
        clear();

        return resultingConstraint;
    }

    public IConstraint generateAssignmentFromPushes(int lineNumber, @NonNull final ISymbol assignmentSymName) {
        boolean isBranch = false;

        // A finalize store constraint is always true
        setEvaluatedToTrue();
        var constraint = generateFromPushes(lineNumber, assignmentSymName);
        constraint.setIsBranch(isBranch);

        return constraint;
    }

    public IConstraint generateBranchFromPushes(int lineNumber) {
        boolean isBranch = true;
        var constraint = generateFromPushes(lineNumber, null);
        constraint.setIsBranch(isBranch);
        return constraint;
    }

    public boolean isReassignment() {
        return valueStack.size() == 1 && operand == null;
    }

    public boolean isCasting() {
        return valueStack.size() == 0 && operand == null;
    }

    public void setEvaluatedToTrue() {
        this.evaluatedResult = true;
    }

    public void setEvaluatedToFalse() {
        this.evaluatedResult = false;
    }

    public boolean isEvaluatedTrue() {
        if (evaluatedResult == null) {
            throw new VBNLibraryRuntimeException("Tried reading evaluation when it was null. Ensure setEvaluatedToTrue() or setEvaluatedToFalse() was run previously");
        }

        return evaluatedResult;
    }

    static public class GenerateConstraintVisitor implements IOperandVisitor {
        private IConstraint generatedConstraint;
        private ISymbol assignSym;

        private int lineNumber;
        private Stack<Value> valueStack;
        private boolean evaluatedResult;

        /**
         * This method should be used rather than setting all the values manually
         * @param valueStack the value stack (cloned)
         * @param evaluatedResult the eval result of the constraint
         * @param assignSym the assignment name (if it exists)
         */
        public void setValues(@NonNull Stack<Value> valueStack, @NonNull Boolean evaluatedResult, int lineNumber, @Nullable ISymbol assignSym) {
            this.assignSym = assignSym;
            this.valueStack = (Stack<Value>) valueStack.clone();
            this.evaluatedResult = evaluatedResult;
            this.lineNumber = lineNumber;
        }

        public void visit(@NonNull BinaryOperand binOp) throws IncorrectNumberOfValuesException {
            assertNumberOfValuesEqual(2);

            var right = valueStack.pop();
            var left = valueStack.pop();

            generatedConstraint = new BinaryConstraint(left, binOp, right, evaluatedResult, lineNumber);

            if (assignSym != null) {
                generatedConstraint.setAssignmentSymbol(assignSym);
            }
        }

        public void visit(@NonNull UnaryOperand unOp) throws IncorrectNumberOfValuesException {
            assertNumberOfValuesEqual(1);

            var symbol = valueStack.pop();
            generatedConstraint = new UnaryConstraint(unOp, symbol, evaluatedResult, lineNumber);

            if (assignSym != null) {
                generatedConstraint.setAssignmentSymbol(assignSym);
            }
        }

        public void visit(@NonNull CustomOperand customOp) throws MissingAssignmentSymbolException, InvalidOperandStrException, IncorrectNumberOfValuesException {
            switch (customOp) {
                case CAST:
                    assertNumberOfValuesEqual(1);
                    break;

                case REASSIGN:
                    if (assignSym == null) {
                        throw new MissingAssignmentSymbolException("Reassign needs an assignment");
                    }

                    assertNumberOfValuesEqual(1);

                    var symbol = valueStack.pop();
                    generatedConstraint = new BinaryConstraint(assignSym, BinaryOperand.EQ, symbol, true, lineNumber);
                    break;

                default:
                    throw new InvalidOperandStrException("There is an Operand that is not covered");
            }
        }

        public void visit(IOperand op) {
            throw new VBNLibraryRuntimeException("An operator is not handled");
        }

        public IConstraint getGeneratedConstraint() {
            return generatedConstraint;
        }

        /**
         * Check the number of values on the stack
         * @param expectedVars the number of values that should be on the stack
         * @throws IncorrectNumberOfValuesException If invalid
         */
        private void assertNumberOfValuesEqual(final int expectedVars) throws IncorrectNumberOfValuesException {
            if (valueStack.size() != expectedVars) {
                throw new IncorrectNumberOfValuesException("Need exactly " + expectedVars + " operand. Found " + valueStack.size() + " values");
            }
        }
    }
}