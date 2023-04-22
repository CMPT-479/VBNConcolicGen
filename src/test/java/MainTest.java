import org.junit.jupiter.api.Assertions;
import vbn.instrument.Instrument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;
import vbn.solver.VBNRunner;

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
        VBNRunner.execute("vbn.examples.Test_00_Basic");
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
        VBNRunner.execute("vbn.examples.Test_02_NEG_vs_MINUS");
        System.out.println("Finished neg vs minus test");
        printSolvedConstraints();
    }

    @Test
    final void basicIf() {
        VBNRunner.execute("vbn.examples.Test_05_If");
        System.out.println("Finished if test");
        printSolvedConstraints();
    }

    @Test
    final void basicIfMultiple() {
        VBNRunner.execute("vbn.examples.Test_06_If_Multiple");
        System.out.println("Finished if multiple test");
        printSolvedConstraints();
    }

    @Test
    final void basicIfMultipleDiffTypes() {
        VBNRunner.execute("vbn.examples.Test_07_If_Multiple_Diff_Types");
        System.out.println("Finished if multiple diff types test");
        printSolvedConstraints();
    }

    @Test
    final void basicIfValue() {
        VBNRunner.execute("vbn.examples.Test_08_If_Value");
        System.out.println("Finished if value test");
        printSolvedConstraints();
    }

    @Test
    final void basicIfDouble() {
        VBNRunner.execute("vbn.examples.Test_09_If_Double");
        System.out.println("Finished if double test");
        printSolvedConstraints();
    }

    @Test
    final void basicIfFloat() {
        VBNRunner.execute("vbn.examples.Test_10_If_Float");
        System.out.println("Finished if float test");
        printSolvedConstraints();
    }
    @Test
    final void testUnmanageable() {
        VBNRunner.execute("vbn.examples.Test_11_Unmanageable");
        System.out.println("Finished unmanageable test");
        printSolvedConstraints();
    }

    @Test
    final void testIncrement() {
        VBNRunner.execute("vbn.examples.Test_12_Increment");
        System.out.println("Finished increment test");
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

}
