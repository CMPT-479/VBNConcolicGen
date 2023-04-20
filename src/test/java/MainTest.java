import vbn.constraints.*;
import vbn.constraints.Value.AbstractSymbolConstant;
import vbn.constraints.Value.Value;
import vbn.instrument.Instrument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;
import vbn.solver.Z3Solver;

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
        final var cute = new Instrument();
        final var transform = new Transform("jtp.Instrument", cute);
        PackManager.v().getPack("jtp").add(transform);
    }

    @Test
    final void basic() {
        final String[] args = new String[] {"vbn.examples.Test_00_Basic"};
        soot.Main.main(args);

        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", "sootOutput" + File.pathSeparator + "target/classes", "vbn.examples.Test_00_Basic", "0", "1");
            // this is currently breaking due to Call finalizeStore not working correctly, but it is actually executing what we want
            /*
            Exception in thread "main" java.lang.NullPointerException
                at vbn.Call.finalizeStore(Call.java:146)
                at vbn.Call.finalizeStore(Call.java:152)
                at vbn.examples.Test_00_Basic.main(Test_00_Basic.java)
            Program exited with code 1
             */

            pb.redirectErrorStream(true);
            Process p = pb.start();
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            p.waitFor(); // wait for the process to finish
            int exitCode = p.exitValue();
            System.out.println("Program exited with code " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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
        state.addSymbol("x", Value.ValueType.BOOL_TYPE);
        state.addSymbol("y", Value.ValueType.BOOL_TYPE);
        try {
            state.pushConstraint(
                    new BinaryConstraint(
                            state.getSymbol("x"),
                            BinaryOperand.OR,
                            state.getSymbol("y")));
        } catch (SymbolMissingException e) {
            System.out.println("Missing symbol from state");
        }

        List<AbstractSymbolConstant> solved = Z3Solver.solve(state);
        Z3Solver.printSolvedValuesBasedOnList(solved);
    }

    @Test
    final void testZ3SimpleAnd() {
        // x and y
        State state = new State();
        state.addSymbol("x", Value.ValueType.BOOL_TYPE);
        state.addSymbol("y", Value.ValueType.BOOL_TYPE);
        try {
            state.pushConstraint(
                    new BinaryConstraint(
                            state.getSymbol("x"),
                            BinaryOperand.AND,
                            state.getSymbol("y")));
        } catch (SymbolMissingException e) {
            System.out.println("Missing symbol from state");
        }

        List<AbstractSymbolConstant> solved = Z3Solver.solve(state);
        Z3Solver.printSolvedValuesBasedOnList(solved);
    }

    @Test
    final void testZ3Gt() {
        // x > y
        State state = new State();
        state.addSymbol("x", Value.ValueType.INT_TYPE);
        state.addSymbol("y", Value.ValueType.INT_TYPE);
        try {
            state.pushConstraint(
                    new BinaryConstraint(
                            state.getSymbol("x"),
                            BinaryOperand.GT,
                            state.getSymbol("y")));
        } catch (SymbolMissingException e) {
            System.out.println("Missing symbol from state");
        }

        List<AbstractSymbolConstant> solved = Z3Solver.solve(state);
        Z3Solver.printSolvedValuesBasedOnList(solved);
    }
}
