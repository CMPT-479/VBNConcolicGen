import org.junit.jupiter.api.Assertions;
import vbn.instrument.Instrument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;
import vbn.solver.VBNRunner;
import vbn.solver.Z3Solver;
import vbn.state.State;
import vbn.state.constraints.BinaryConstraint;
import vbn.state.constraints.BinaryOperand;
import vbn.state.value.AbstractSymbol;
import vbn.state.value.BooleanSymbol;
import vbn.state.value.IntSymbol;

import java.io.*;
import java.util.List;

public class MainTest {

    @BeforeEach
    final void initializeSoot() {
        G.reset();
        final String[] paths = {
                ".",
                new File("target", "classes").toString(),
                "VIRTUAL_FS_FOR_JDK",
        };

        Options.v().set_soot_classpath(String.join(File.pathSeparator, paths));
        Options.v().set_keep_line_number(true);
        final var cute = new Instrument();
        final var transform = new Transform("jtp.Instrument", cute);
        PackManager.v().getPack("jtp").add(transform);
    }

    @Test
    final void basic() {
        var exitCode = VBNRunner.execute("vbn.examples.Test_00_Basic");
        Assertions.assertEquals(exitCode, 0);
    }

    @Test
    final void basicArray() {
        final String[] args = new String[] {"vbn.examples.Test_01_Array"};
        soot.Main.main(args);
    }

    @Test
    final void basicNegAndMinus() {
        final String[] args = new String[] {"vbn.examples.Test_02_NEG_vs_MINUS"};
        soot.Main.main(args);
    }

    @Test
    final void basicClass() {
        final String[] args = new String[] {"vbn.examples.Test_03_Class"};
        soot.Main.main(args);
    }

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

        List<AbstractSymbol> solved = Z3Solver.solve(state);
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

        List<AbstractSymbol> solved = Z3Solver.solve(state);
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

        List<AbstractSymbol> solved = Z3Solver.solve(state);
        Z3Solver.printSolvedValuesBasedOnList(solved);
    }
}
