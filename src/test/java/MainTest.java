import org.cmpt479.instrument.CuteTransformer;
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
        final var cute = new CuteTransformer();
        final var transform = new Transform("jtp.CuteTransformer", cute);
        PackManager.v().getPack("jtp").add(transform);
    }

    @Test
    final void basic() {
        final String[] args = new String[] {"org.cmpt479.examples.Test_00_Basic"};
        soot.Main.main(args);
    }
}
