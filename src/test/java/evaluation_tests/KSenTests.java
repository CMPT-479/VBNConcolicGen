package evaluation_tests;

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

public class KSenTests {
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
    final void ksenIf() {
        VBNRunner.execute("vbn.eval_test_files.ksen_tests.If");
        printAndValidateSolvedConstraints(2);
    }

    @Test
    final void ksenLinear() {
        VBNRunner.execute("vbn.eval_test_files.ksen_tests.Linear");
        printAndValidateSolvedConstraints(4);
    }

    @Test
    final void ksenNonlinear() {
        VBNRunner.execute("vbn.eval_test_files.ksen_tests.Nonlinear");
        printAndValidateSolvedConstraints(3);
    }

    @Test
    final void testUnmanageable() {
        VBNRunner.execute("vbn.eval_test_files.ksen_tests.Unmanageable");
        System.out.println("Finished unmanageable test");
        printAndValidateSolvedConstraints(2);
    }

    @Test
    final void testTestme() {
        VBNRunner.execute("vbn.eval_test_files.ksen_tests.Testme");
        System.out.println("Finished testme test");
        printAndValidateSolvedConstraints(3);
    }

    @Test
    final void testInterfaceTest() {
        VBNRunner.execute("vbn.eval_test_files.ksen_tests.InterfaceTest");
        System.out.println("Finished interfacetest test");
        printAndValidateSolvedConstraints(2);
    }

}
