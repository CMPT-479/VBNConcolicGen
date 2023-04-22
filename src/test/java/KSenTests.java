import org.junit.jupiter.api.BeforeEach;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;
import vbn.instrument.Instrument;
import vbn.solver.VBNRunner;

import java.io.File;

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


}
