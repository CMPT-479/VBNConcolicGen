package vbn.state.helpers;

import lombok.NonNull;
import org.junit.Test;
import vbn.state.constraints.*;
import vbn.state.value.AbstractConstant;
import vbn.state.value.AbstractSymbol;
import vbn.state.value.BooleanSymbol;
import vbn.state.value.Value;

import javax.annotation.Nullable;
import java.util.Stack;

import static org.junit.Assert.*;

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

            opVisitor.assignSym = assignmentSymName;
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

    static public class GenerateConstraintVisitor implements IOperandVisitor {
        private AbstractConstraint generatedConstraint;
        public AbstractSymbol assignSym;
        public Stack<Value> valueStack;

        public void visit(BinaryOperand binOp) throws IncorrectNumberOfValuesException {
            assertNumberOfValuesEqual(2);

            var right = valueStack.pop();
            var left = valueStack.pop();
            if (assignSym == null) {
                generatedConstraint = new BinaryConstraint(left, binOp, right);
            }
            else {
                generatedConstraint = new BinaryConstraint(left, binOp, right, assignSym);
            }
        }

        public void visit(UnaryOperand unOp) throws IncorrectNumberOfValuesException {
            assertNumberOfValuesEqual(1);

            var symbol = valueStack.pop();
            if (assignSym == null) {
                generatedConstraint = new UnaryConstraint(unOp, symbol);
            }
            else {
                generatedConstraint = new UnaryConstraint(unOp, symbol, assignSym);
            }
        }

        public void visit(CustomOperand customOp) throws MissingAssignmentSymbolException, InvalidOperandStrException, IncorrectNumberOfValuesException {
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
                    generatedConstraint = new BinaryConstraint(assignSym, BinaryOperand.EQ, symbol);
                    break;

                default:
                    throw new InvalidOperandStrException("There is an Operand that is not covered");
            }
        }

        public void visit(IOperand op) {
            throw new RuntimeException("An operator is not handled");
        }

        public AbstractConstraint getGeneratedConstraint() {
            return generatedConstraint;
        }

        /**
         * Check the number of values on the stack
         * @param expectedVars the number of values that should be on the stack
         * @throws IncorrectNumberOfValuesException If invalid
         */
        private void assertNumberOfValuesEqual(final int expectedVars) throws IncorrectNumberOfValuesException {
            if (valueStack.size() < expectedVars) {
                throw new IncorrectNumberOfValuesException("Need exactly " + expectedVars + " operand. Found less than " + expectedVars + ".");
            }
            if (valueStack.size() > expectedVars) {
                throw new IncorrectNumberOfValuesException("Need exactly " + expectedVars + " operand. Found more than " + expectedVars + ".");
            }
        }
    }

    @Test
    public void testSimpleCompute() {
        clear();

        var boolX = new BooleanSymbol("x", true);
        var boolY = new BooleanSymbol("y", false);

        pushSymbol(boolX);
        pushSymbol(boolY);
        setOperand(BinaryOperand.AND);

        var result = generateFromPushes();
        var groundTruth = new BinaryConstraint(boolX, BinaryOperand.AND, boolY);
        assertEquals(result, groundTruth);

        clear();
    }

    @Test
    public void testCustomOperand() {
        clear();

        var boolX = new BooleanSymbol("x", true);
        var boolY = new BooleanSymbol("y", false);

        pushSymbol(boolY);
        setOperand(CustomOperand.REASSIGN);

        var result = generateFromPushes(boolX);
        var groundTruth = new BinaryConstraint(boolX, BinaryOperand.EQ, boolY);
        assertEquals(result, groundTruth);

        clear();
    }
}