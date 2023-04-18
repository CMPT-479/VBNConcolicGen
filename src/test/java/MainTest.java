import com.microsoft.z3.*;
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
        final String[] args = new String[] {"org.cmpt479.examples.Test_00_Basic"};
        soot.Main.main(args);
    }

    @Test
    final void testZ3() {
        // Create Z3 solver instance
        System.out.println("================ TESTING Z3 ================");
        Context ctx = new Context();
        Solver solver = ctx.mkSolver();

        // Create logical expression
        BoolExpr x = ctx.mkBoolConst("x");
        BoolExpr y = ctx.mkBoolConst("y");
        BoolExpr p1 = ctx.mkOr(x, y);
        BoolExpr p2 = ctx.mkOr(ctx.mkNot(x), y);
        BoolExpr pathConstraint = ctx.mkAnd(p1, p2);

        // Add path constraint to solver
        solver.add(pathConstraint);

        // Check for satisfying assignment
        Status status = solver.check();
        if (status == Status.SATISFIABLE) {
            // Get satisfying assignment
            Model model = solver.getModel();
            System.out.println("x = " + model.eval(x, true));
            System.out.println("y = " + model.eval(y, true));
        } else if (status == Status.UNSATISFIABLE) {
            System.out.println("Path constraint is unsatisfiable");
        } else {
            System.out.println("Solver did not return a satisfying assignment");
        }

        // Dispose of solver instance
        System.gc();
        ctx.close();
    }
}
