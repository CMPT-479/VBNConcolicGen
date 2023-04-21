import org.junit.jupiter.api.Test;
import vbn.solver.Z3Solver;
import vbn.state.State;
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
        State state = new State();
        state.addSymbol(new BooleanSymbol("x", false));  // concrete value doesn't matter
        state.addSymbol(new BooleanSymbol("y", false));  // concrete value doesn't matter
        state.pushConstraint(
                new BinaryConstraint(
                        state.getSymbol("x"),
                        BinaryOperand.OR,
                        state.getSymbol("y")));

        List<ISymbol> solved = Z3Solver.solve(state);
        Z3Solver.printSolvedValuesBasedOnList(solved);
    }

    @Test
    final void testZ3SimpleAnd() {
        // x and y
        State state = new State();
        state.addSymbol(new BooleanSymbol("x", false));  // concrete value doesn't matter
        state.addSymbol(new BooleanSymbol("y", false));  // concrete value doesn't matter
        state.pushConstraint(
                new BinaryConstraint(
                        state.getSymbol("x"),
                        BinaryOperand.AND,
                        state.getSymbol("y")));

        List<ISymbol> solved = Z3Solver.solve(state);
        Z3Solver.printSolvedValuesBasedOnList(solved);
    }

    @Test
    final void testZ3Gt() {
        // x > y
        State state = new State();
        state.addSymbol(new IntSymbol("x", 1));  // concrete value doesn't matter
        state.addSymbol(new IntSymbol("y", 1));  // concrete value doesn't matter
        state.pushConstraint(
                new BinaryConstraint(
                        state.getSymbol("x"),
                        BinaryOperand.GT,
                        state.getSymbol("y")));

        List<ISymbol> solved = Z3Solver.solve(state);
        Z3Solver.printSolvedValuesBasedOnList(solved);
    }
}
