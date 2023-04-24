import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;
import vbn.instrument.Instrument;
import vbn.solver.VBNRunner;

import java.io.File;

import static vbn.solver.VBNRunner.printAndValidateSolvedConstraints;

public class CrestTests {
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
        VBNRunner.reset();
    }

    @Test
    final void crestUnary() {
        VBNRunner.execute("vbn.crest_tests.Unary");
        printAndValidateSolvedConstraints(2);
    }
    @Test
    final void crestMath() {
        VBNRunner.execute("vbn.crest_tests.Math");
        printAndValidateSolvedConstraints(2);
    }
    @Test
    final void crestSimple() {
        VBNRunner.execute("vbn.crest_tests.Simple");
        printAndValidateSolvedConstraints(2);
    }

    @Test
    final void crestUniform() {
        VBNRunner.execute("vbn.crest_tests.UniformTest");
        printAndValidateSolvedConstraints(5);
    }

    @Test
    final void crestStructure() {
        VBNRunner.execute("vbn.crest_tests.Structure");
        printAndValidateSolvedConstraints(2);
    }

    @Test
    final void crestFunction() {
        VBNRunner.execute("vbn.crest_tests.Function");
        printAndValidateSolvedConstraints(2);
    }

    @Test
    final void crestCFG() {
        VBNRunner.execute("vbn.crest_tests.CFG");
        printAndValidateSolvedConstraints(0);
    }
}
