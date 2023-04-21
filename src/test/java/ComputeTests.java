import org.junit.Test;
import vbn.state.constraints.BinaryConstraint;
import vbn.state.constraints.BinaryOperand;
import vbn.state.constraints.CustomOperand;
import vbn.state.helpers.ComputeConstraints;
import vbn.state.value.BooleanSymbol;

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

        var result = constraintComputer.generateFromPushes();
        var groundTruth = new BinaryConstraint(boolX, BinaryOperand.AND, boolY, false);
        assertEquals(result, groundTruth);
    }
    @Test
    public void testCustomOperand() {
        var constraintComputer = new ComputeConstraints();

        var boolX = new BooleanSymbol("x", true);
        var boolY = new BooleanSymbol("y", false);

        constraintComputer.pushSymbol(boolY);
        constraintComputer.setOperand(CustomOperand.REASSIGN);

        var result = constraintComputer.generateFromPushes(boolX);
        var groundTruth = new BinaryConstraint(boolX, BinaryOperand.EQ, boolY, false);
        assertEquals(result, groundTruth);
    }
}
