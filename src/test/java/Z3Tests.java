import org.junit.jupiter.api.Test;
import vbn.solver.Z3Solver;
import vbn.state.GlobalState;
import vbn.state.constraints.BinaryConstraint;
import vbn.state.constraints.BinaryOperand;
import vbn.state.value.ISymbol;
import vbn.state.value.BooleanSymbol;
import vbn.state.value.IntSymbol;

import java.util.List;

public class Z3Tests {
    @Test
    final void testZ3SimpleOr() {
        // x or y
        GlobalState globalState = new GlobalState();
        globalState.addSymbol(new BooleanSymbol("x", false));  // concrete value doesn't matter
        globalState.addSymbol(new BooleanSymbol("y", false));  // concrete value doesn't matter
        globalState.pushConstraint(
                new BinaryConstraint(
                        globalState.getSymbol("x"),
                        BinaryOperand.OR,
                        globalState.getSymbol("y"), false, -1));

        List<ISymbol> solved = Z3Solver.solve(globalState);
        Z3Solver.printSolvedValuesBasedOnList(solved);
    }

    @Test
    final void testZ3SimpleAnd() {
        // x and y
        GlobalState globalState = new GlobalState();
        globalState.addSymbol(new BooleanSymbol("x", false));  // concrete value doesn't matter
        globalState.addSymbol(new BooleanSymbol("y", false));  // concrete value doesn't matter
        globalState.pushConstraint(
                new BinaryConstraint(
                        globalState.getSymbol("x"),
                        BinaryOperand.AND,
                        globalState.getSymbol("y"), false, -1));

        List<ISymbol> solved = Z3Solver.solve(globalState);
        Z3Solver.printSolvedValuesBasedOnList(solved);
    }

    @Test
    final void testZ3Gt() {
        // x > y
        GlobalState globalState = new GlobalState();
        globalState.addSymbol(new IntSymbol("x", 1));  // concrete value doesn't matter
        globalState.addSymbol(new IntSymbol("y", 1));  // concrete value doesn't matter
        globalState.pushConstraint(
                new BinaryConstraint(
                        globalState.getSymbol("x"),
                        BinaryOperand.GT,
                        globalState.getSymbol("y"), false, -1));

        List<ISymbol> solved = Z3Solver.solve(globalState);
        Z3Solver.printSolvedValuesBasedOnList(solved);
    }
}
