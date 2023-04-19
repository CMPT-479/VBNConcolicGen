import vbn.constraints.BoolOperand;
import vbn.constraints.ConstraintItemBool;
import vbn.constraints.State;
import vbn.constraints.Symbol;
import vbn.instrument.Instrument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;
import vbn.solver.Z3Solver;

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

    @Test
    final void testZ3() {
        State state = new State();
        state.addSymbol("x", Symbol.SymbolType.BOOL_TYPE);
        state.addSymbol("y", Symbol.SymbolType.BOOL_TYPE);
        state.pushConstraints(new ConstraintItemBool(state.getSymbol("x"), BoolOperand.OR, state.getSymbol("y")));
        Z3Solver z3Solver = new Z3Solver();
        z3Solver.solve(state);
    }
}
