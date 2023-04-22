import org.junit.Test;
import vbn.state.State;
import vbn.state.constraints.BinaryConstraint;
import vbn.state.constraints.BinaryOperand;
import vbn.state.constraints.CustomOperand;
import vbn.state.constraints.IConstraint;
import vbn.state.helpers.ComputeConstraints;
import vbn.state.value.*;

import java.util.Stack;

import static org.junit.Assert.assertEquals;

public class ComputeTests {

    @Test
    public void testSimpleCompute() {
        var constraintComputer = new ComputeConstraints();

        var boolX = new BooleanSymbol("x", true);
        var boolY = new BooleanSymbol("y", false);

        constraintComputer.pushSymbol(boolX);
        constraintComputer.pushSymbol(boolY);
        constraintComputer.setOperand(BinaryOperand.AND);

        var result = constraintComputer.generateFromPushes(-1, null);
        var groundTruth = new BinaryConstraint(boolX, BinaryOperand.AND, boolY, false, -1);
        assertEquals(result, groundTruth);
    }

    @Test
    public void testCustomOperand() {
        var constraintComputer = new ComputeConstraints();

        var boolX = new BooleanSymbol("x", true);
        var boolY = new BooleanSymbol("y", false);

        constraintComputer.pushSymbol(boolY);
        constraintComputer.setOperand(CustomOperand.REASSIGN);

        var result = constraintComputer.generateFromPushes(0, boolX);
        var groundTruth = new BinaryConstraint(boolX, BinaryOperand.EQ, boolY, false, -1);
        assertEquals(result, groundTruth);
    }

    @Test
    public void testBinaryConstraintNormalInt() {
        var visitor = new ComputeConstraints.GenerateConstraintVisitor();
        Stack<Value> valueStack = new Stack<>();
        ISymbol left;
        ISymbol right;
        BinaryOperand operand;
        BinaryConstraint groundTruth;

        // Normal Ints Binary
        left = new IntSymbol("left", 4);
        right = new IntSymbol("right", 5);
        operand = BinaryOperand.EQ;

        groundTruth = new BinaryConstraint(left, operand, right, false, -1);

        valueStack.push(left);
        valueStack.push(right);
        visitor.setValues(valueStack, false, -1, null);
        operand.accept(visitor);
        assertEquals(groundTruth, visitor.getGeneratedConstraint());

    }

    @Test
    public void testBinaryConstraintNormalBool() {
        var visitor = new ComputeConstraints.GenerateConstraintVisitor();
        Stack<Value> valueStack = new Stack<>();
        ISymbol left;
        ISymbol right;
        BinaryOperand operand;
        BinaryConstraint groundTruth;

        // Normal Boolean Binary
        left = new BooleanSymbol("left", true);
        right = new BooleanSymbol("right", false);
        operand = BinaryOperand.GT;

        groundTruth = new BinaryConstraint(left, operand, right, false, -1);

        valueStack.push(left);
        valueStack.push(right);
        visitor.setValues(valueStack, false, -1, null);
        operand.accept(visitor);

        assertEquals(groundTruth, visitor.getGeneratedConstraint());
    }

    /**
     * Tests mixing up a Boolean symbol with an Int Constant = 1 or 0
     */
    @Test
    public void testBinaryConstraintBooleanIntMix_IntThenBool() {
        State state = new State();
        Value left;
        Value right;
        BooleanConstant boolSymbol;
        BinaryOperand operand;

        BinaryConstraint groundTruth;
        BinaryConstraint initialConstraint;
        IConstraint finalConstraint;

        // Left is the constant
        left = new IntConstant(1);
        right = new BooleanSymbol("left", true);
        boolSymbol = new BooleanConstant(true);
        operand = BinaryOperand.EQ;

        initialConstraint = new BinaryConstraint(left, operand, right, false, -1);
        state.pushConstraint(initialConstraint);

        finalConstraint = state.getConstraints().pop();
        groundTruth = new BinaryConstraint(left, operand, boolSymbol, false, -1);

        assertEquals(groundTruth.left.getType(), ((BinaryConstraint) finalConstraint).left.getType());
        assertEquals(groundTruth.left.getValue(), ((BinaryConstraint) finalConstraint).left.getValue());
        assertEquals(groundTruth.right.getType(), ((BinaryConstraint) finalConstraint).right.getType());
        assertEquals(groundTruth.right.getValue(), ((BinaryConstraint) finalConstraint).right.getValue());
    }
    @Test
    public void testBinaryConstraintBooleanIntMix_BoolThenInt() {
        State state = new State();
        Value left;
        Value right;
        BooleanConstant boolSymbol;
        BinaryOperand operand;

        BinaryConstraint groundTruth;
        BinaryConstraint initialConstraint;
        IConstraint finalConstraint;

        // Left is the constant
        left = new BooleanSymbol("left", true);
        right = new IntConstant(1);
        boolSymbol = new BooleanConstant(true);
        operand = BinaryOperand.EQ;

        initialConstraint = new BinaryConstraint(left, operand, right, false, -1);
        state.pushConstraint(initialConstraint);

        finalConstraint = state.getConstraints().pop();
        groundTruth = new BinaryConstraint(left, operand, boolSymbol, false, -1);

        assertEquals(groundTruth.left.getType(), ((BinaryConstraint) finalConstraint).left.getType());
        assertEquals(groundTruth.left.getValue(), ((BinaryConstraint) finalConstraint).left.getValue());
        assertEquals(groundTruth.right.getType(), ((BinaryConstraint) finalConstraint).right.getType());
        assertEquals(groundTruth.right.getValue(), ((BinaryConstraint) finalConstraint).right.getValue());

    }

    @Test
    public void testAssignmentSymbolIsIntWhenItShouldBeBoolean() {
        // TODO: Test that this works properly
    }

    /**
     * Tests to test setting the evaluated result properly
     */

    @Test
    public void testSetEvaluatedResult() {
        var visitor = new ComputeConstraints.GenerateConstraintVisitor();
        Stack<Value> valueStack = new Stack<>();
        Value left;
        Value right;
        BinaryOperand operand;
        BinaryConstraint groundTruth;
        boolean evaluatedResult;

        // Left is the constant
        left = new IntConstant(2);
        right = new IntConstant(1);
        operand = BinaryOperand.EQ;
        evaluatedResult = true;

        groundTruth = new BinaryConstraint(left, operand, right, evaluatedResult, -1);

        valueStack.push(left);
        valueStack.push(right);
        visitor.setValues(valueStack, evaluatedResult, -1, null);
        operand.accept(visitor);

        assertEquals(groundTruth.getOriginalEvaluation(), ((BinaryConstraint) visitor.getGeneratedConstraint()).getOriginalEvaluation());
    }
}
