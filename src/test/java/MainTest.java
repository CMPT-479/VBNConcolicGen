import vbn.instrument.Instrument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;

import java.io.File;

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
    }

//    @Test
//    final void testZ3SimpleOr() {
//        State state = new State();
//        state.addSymbol("x", Symbol.SymbolType.BOOL_TYPE);
//        state.addSymbol("y", Symbol.SymbolType.BOOL_TYPE);
//        state.pushConstraints(new ConstraintItemBool(state.getSymbol("x"), BoolBinaryCompare.OR, state.getSymbol("y")));
//        Z3Solver.solve(state);
//    }
//
//    @Test
//    final void testZ3SimpleAnd() {
//        State state = new State();
//        state.addSymbol("x", Symbol.SymbolType.BOOL_TYPE);
//        state.addSymbol("y", Symbol.SymbolType.BOOL_TYPE);
//        state.pushConstraints(new ConstraintItemBool(state.getSymbol("x"), BoolOperand.AND, state.getSymbol("y")));
//        Z3Solver.solve(state);
//    }
}
