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

    @Test
    public void BasicIfTest() {
        try {
            String[] var0 = {"12", "123", "1234"};
            var x = 1;
            var y = 1;

            Call.init();
            Call.pushSym("r0[0]", var0[0]);
            String var1;
            String var10000 = var1 = var0[0];
            Call.finalizeStore("$r1", var1);
            int var9 = Integer.parseInt(var10000);
            Integer var6 = var9;
            Call.pushSym("$i0", var6);
            x = var9;
            Call.finalizeStore("vbn.examples.Test_05_If.x", x);
            Call.pushSym("r0[1]", var0[1]);
            String var3;
            var10000 = var3 = var0[1];
            Call.finalizeStore("$r2", var3);
            var9 = Integer.parseInt(var10000);
            Integer var4 = var9;
            Call.pushSym("$i1", var4);
            y = var9;
            Call.finalizeStore("vbn.examples.Test_05_If.y", y);
            Call.pushSym("vbn.examples.Test_05_If.x", x);
            int var8 = x;
            Call.finalizeStore("$i3", var8);
            Call.pushSym("vbn.examples.Test_05_If.y", y);
            int var5 = y;
            Call.finalizeStore("$i2", var5);
            Call.pushSym("$i3", var8);
            Call.pushSym("$i2", var5);
            Call.apply(" <= ");
            Call.finalizeIf(9);
            PrintStream var7;
            PrintStream var10;
            if (var8 > var5) {
                Call.pushSym("java.lang.System.out", System.out);
                var10 = var7 = System.out;
                Call.finalizeStore("$r4", var7);
                var10.println("COOL");
            } else {
                Call.pushSym("java.lang.System.out", System.out);
                var10 = var7 = System.out;
                Call.finalizeStore("$r3", var7);
                var10.println("NICE");
            }

            Call.terminatePath(14);
        } catch (Throwable var2) {
            Call.terminatedWithError(var2);
        }
    }
}
