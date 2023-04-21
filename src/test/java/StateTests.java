import org.junit.jupiter.api.Test;
import vbn.Call;

import java.io.PrintStream;

public class StateTests {
    @Test
    public void BasicTestInstrumentation() {
        Call.init();

        String[] var0 = {"12", "123", "1234"};

        Call.pushSym("randomVar", 0);
        Call.finalizeStore("i0", var0.length);
        Call.pushSym("r0[0]", var0[0]);
        String var1;
        String var10000 = var1 = var0[0];
        Call.finalizeStore("$r1", var1);
    }
}
