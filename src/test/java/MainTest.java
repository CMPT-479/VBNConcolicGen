import org.cmpt479.instrument.CuteTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;

public class MainTest {
    @BeforeEach
    final void initializeSoot() {
        G.reset();
        Options.v().set_soot_classpath(".:target/classes:VIRTUAL_FS_FOR_JDK");
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
