package vbn.state.helpers;

import lombok.NonNull;
import org.junit.Test;
import vbn.state.VBNLibraryRuntimeException;
import vbn.state.constraints.*;
import vbn.state.value.*;

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
    }

    /**
     * Generate constraints based on the calls
     * @param assignmentSymName The symbol name to assign the constraints to.
     */
    public IConstraint generateFromPushes(@Nullable final ISymbol assignmentSymName) {
        IConstraint resultingConstraint;

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
            throw new VBNLibraryRuntimeException(e);
        }
        return resultingConstraint;
    }

    public IConstraint generateFromPushes(int lineNumber, @Nullable final ISymbol assignmentSymName) {
        var constraint = generateFromPushes(assignmentSymName);
        constraint.setLineNumber(lineNumber);
        return constraint;
    }

    /**
     * Generate constraints based on the calls
     *
     * @return the newly created constraint
     */
    public IConstraint generateFromPushes() {
        return generateFromPushes(null);
    }

    public boolean isReassignment() {
        return valueStack.size() == 1 && operand == null;
    }

    static public class GenerateConstraintVisitor implements IOperandVisitor {
        private IConstraint generatedConstraint;
        public ISymbol assignSym;
        public Stack<Value> valueStack;

        public void visit(@NonNull BinaryOperand binOp) throws IncorrectNumberOfValuesException {
            assertNumberOfValuesEqual(2);

            var right = valueStack.pop();
            var left = valueStack.pop();

            // Jimple converts booleans into an int that is one or zero
            // We need to convert it back into an boolean
            right = handleBoolAndIntComparison(left, binOp, right);
            left = handleBoolAndIntComparison(right, binOp, left);

            if (right.getType() != left.getType()) {
                throw new VBNLibraryRuntimeException("The value types are not equal when creating a constraint");
            }

            if (assignSym == null) {
                generatedConstraint = new BinaryConstraint(left, binOp, right);
            }
            else {
                generatedConstraint = new BinaryConstraint(left, binOp, right, assignSym);
            }
        }

        /**
         * Jimple converts booleans into an int that is one or zero (e.g. if (BOOL == 0))
         *
         * @param boolSymbol the boolean symbol
         * @param binOp the operation to confirm is equal
         * @param intSymbol the int symbol to convert to a bool symbol
         * @return the int symbol, potentially converted to a bool symbol
         */
        private static Value handleBoolAndIntComparison(@NonNull Value boolSymbol, @NonNull BinaryOperand binOp, @NonNull Value intSymbol) {
            if (boolSymbol instanceof BooleanSymbol && intSymbol instanceof IntConstant) {
                int leftValue = (int) intSymbol.getValue();
                if (!(leftValue == 0 || leftValue == 1)) {
                    throw new VBNLibraryRuntimeException("The int value handled must be 0 or 1 when converting to bool");
                }
                if (!(binOp == BinaryOperand.EQ)) {
                    throw new VBNLibraryRuntimeException("The operator must be 'equal to' when converting ");
                }
                intSymbol = new BooleanConstant(leftValue == 1);
            }
            return intSymbol;
        }

        public void visit(@NonNull UnaryOperand unOp) throws IncorrectNumberOfValuesException {
            assertNumberOfValuesEqual(1);

            var symbol = valueStack.pop();
            if (assignSym == null) {
                generatedConstraint = new UnaryConstraint(unOp, symbol);
            }
            else {
                generatedConstraint = new UnaryConstraint(unOp, symbol, assignSym);
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
                    generatedConstraint = new BinaryConstraint(assignSym, BinaryOperand.EQ, symbol);
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
            if (valueStack.size() < expectedVars) {
                throw new IncorrectNumberOfValuesException("Need exactly " + expectedVars + " operand. Found less than " + expectedVars + ".");
            }
            if (valueStack.size() > expectedVars) {
                throw new IncorrectNumberOfValuesException("Need exactly " + expectedVars + " operand. Found more than " + expectedVars + ".");
            }
        }

        @Test
        public void testBinaryConstraintNormal() {
            var visitor = new GenerateConstraintVisitor();
            ISymbol left;
            ISymbol right;
            BinaryOperand operand;
            BinaryConstraint groundTruth;

            // Normal Ints Binary
            left = new IntSymbol("left", 4);
            right = new IntSymbol("right", 5);
            operand = BinaryOperand.EQ;

            groundTruth = new BinaryConstraint(left, operand, right);

            visitor.valueStack = new Stack<>();
            visitor.valueStack.push(left);
            visitor.valueStack.push(right);
            operand.accept(visitor);

            assertEquals(groundTruth, visitor.generatedConstraint);

            // Normal Boolean Binary
            left = new BooleanSymbol("left", true);
            right = new BooleanSymbol("right", false);
            operand = BinaryOperand.GT;

            groundTruth = new BinaryConstraint(left, operand, right);

            visitor.valueStack = new Stack<>();
            visitor.valueStack.push(left);
            visitor.valueStack.push(right);
            operand.accept(visitor);

            assertEquals(groundTruth, visitor.generatedConstraint);
        }
        @Test
        public void testBinaryConstraintBooleanIntMix() {
            var visitor = new GenerateConstraintVisitor();
            Value left;
            Value right;
            BooleanConstant boolSymbol;
            BinaryOperand operand;
            BinaryConstraint groundTruth;

            // Right is the constant
            left = new IntConstant(0);
            boolSymbol = new BooleanConstant(false);
            right = new BooleanSymbol("right", true);
            operand = BinaryOperand.EQ;

            groundTruth = new BinaryConstraint(boolSymbol, operand, right);

            visitor.valueStack = new Stack<>();
            visitor.valueStack.push(left);
            visitor.valueStack.push(right);
            operand.accept(visitor);

            assertEquals(groundTruth.left.getType(), ((BinaryConstraint) visitor.generatedConstraint).left.getType());
            assertEquals(groundTruth.left.getValue(), ((BinaryConstraint) visitor.generatedConstraint).left.getValue());
            assertEquals(groundTruth.right.getType(), ((BinaryConstraint) visitor.generatedConstraint).right.getType());
            assertEquals(groundTruth.right.getValue(), ((BinaryConstraint) visitor.generatedConstraint).right.getValue());

            // Left is the constant
            left = new BooleanSymbol("left", true);
            right = new IntConstant(1);
            boolSymbol = new BooleanConstant(true);
            operand = BinaryOperand.EQ;

            groundTruth = new BinaryConstraint(left, operand, boolSymbol);

            visitor.valueStack = new Stack<>();
            visitor.valueStack.push(left);
            visitor.valueStack.push(right);
            operand.accept(visitor);

            assertEquals(groundTruth.left.getType(), ((BinaryConstraint) visitor.generatedConstraint).left.getType());
            assertEquals(groundTruth.left.getValue(), ((BinaryConstraint) visitor.generatedConstraint).left.getValue());
            assertEquals(groundTruth.right.getType(), ((BinaryConstraint) visitor.generatedConstraint).right.getType());
            assertEquals(groundTruth.right.getValue(), ((BinaryConstraint) visitor.generatedConstraint).right.getValue());
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