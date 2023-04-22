import org.junit.jupiter.api.Assertions;
import vbn.instrument.Instrument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;
import vbn.solver.VBNRunner;
import vbn.state.VBNLibraryRuntimeException;

import java.io.*;

import static vbn.solver.VBNRunner.printSolvedConstraints;

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
        VBNRunner.reset();
    }

    @Test
    final void basic() {
        VBNRunnerTestHelper("vbn.examples.Test_00_Basic");
        System.out.println("Finished basic test");
        printSolvedConstraints();
    }

    @Test
    final void basicArray() {
        final String[] args = new String[] {"vbn.examples.Test_01_Array"};
        soot.Main.main(args);
    }

    @Test
    final void basicNegAndMinus() {
        VBNRunnerTestHelper("vbn.examples.Test_02_NEG_vs_MINUS");
        System.out.println("Finished neg vs minus test");
        printSolvedConstraints();
    }

    @Test
    final void basicIf() {
        VBNRunnerTestHelper("vbn.examples.Test_05_If");
        System.out.println("Finished if test");
        printSolvedConstraints();
    }

    @Test
    final void basicIfMultiple() {
        VBNRunnerTestHelper("vbn.examples.Test_06_If_Multiple");
        System.out.println("Finished if multiple test");
        printSolvedConstraints();
    }

    @Test
    final void basicIfMultipleDiffTypes() {
        VBNRunnerTestHelper("vbn.examples.Test_07_If_Multiple_Diff_Types");
        System.out.println("Finished if multiple test");
        printSolvedConstraints();
    }

    @Test
    final void basicIfValue() {
        VBNRunnerTestHelper("vbn.examples.Test_08_If_Value");
        System.out.println("Finished if value test");
        printSolvedConstraints();
    }

    @Test
    final void basicIfDouble() {
        System.out.println("Finished if double test");
        printSolvedConstraints();
    }

    @Test
    final void basicIfFloat() {
        VBNRunnerTestHelper("vbn.examples.Test_10_If_Float");
        System.out.println("Finished if float test");
        printSolvedConstraints();
    }

    @Test
    final void basicClass() {
        final String[] args = new String[] {"vbn.examples.Test_03_Class"};
        soot.Main.main(args);
    }

    @Test
    final void basicFunction() {
        final String[] args = new String[] {"vbn.examples.Test_08_Function"};
        soot.Main.main(args);
    }

    private void VBNRunnerTestHelper(String programName) {
        try {
            var time = System.currentTimeMillis();
            System.out.println(time);
            VBNRunner.execute("vbn.examples.Test_09_If_Double", time);
        } catch (VBNLibraryRuntimeException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

}
