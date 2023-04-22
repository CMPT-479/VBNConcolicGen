import org.junit.jupiter.api.Test;
import vbn.Call;

import java.io.PrintStream;

public class StateTests {
    @Test
    public void basicTest__If_Basic() {
        Call.initTestingMode();
        String[] var0 = {"12", "123", "1234"};
        var x = 1;

        try {
            Call.init();
            Call.popArg("r0", var0);
            Call.pushSym("r0[0]", var0[0]);
            String var2;
            String var10000 = var2 = var0[0];
            Call.finalizeStore("$r1", var2, 7);
            int var3 = Integer.parseInt(var10000);
            Call.popArg("$i0", var3);
            Call.finalizeReturn("$i0", var3, 7);
            Call.pushSym("$i0", var3);
            x = var3;
            Call.finalizeStore("vbn.examples.Test_12_Increment.x", x, 7);
            Call.pushSym("vbn.examples.Test_12_Increment.x", x);
            var3 = x;
            Call.finalizeStore("$i1", var3, 8);
            Call.pushSym("$i1", var3);
            Call.pushConstant(1);
            Call.apply(" + ");
            ++var3;
            Call.finalizeStore("$i2", var3, 8);
            Call.pushSym("$i2", var3);
            x = var3;
            Call.finalizeStore("vbn.examples.Test_12_Increment.x", x, 8);
            Call.terminatePath(9);
        } catch (Throwable var1) {
            Call.terminatedWithError(var1);
        }



    }
}
