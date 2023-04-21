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
        int var5 = Integer.parseInt(var10000);
        Call.finalizeStore("$i1", var5);
        Call.pushSym("r0[1]", var0[1]);
        String var3;
        var10000 = var3 = var0[1];
        Call.finalizeStore("$r2", var3);
        int var4 = Integer.parseInt(var10000);
        Call.finalizeStore("$i2", var4);
        Call.pushSym("$i1", var5);
        Call.pushSym("$i2", var4);
        Call.apply(" + ");
        var4 += var5;
        Call.finalizeStore("$i3", var4);
        Call.pushSym("$i3", var4);
        Call.pushConstant(3);
        Call.apply(" * ");
        var4 *= 3;
        Call.finalizeStore("i4", var4);
        Call.pushSym("i4", var4);
        Call.pushConstant(1);
        Call.apply(" + ");
        ++var4;
        Call.finalizeStore("i5", var4);
        Call.pushSym("java.lang.System.out", System.out);
        PrintStream var6;
        PrintStream var7 = var6 = System.out;
        Call.finalizeStore("$r3", var6);
        var7.println(var4);
        Call.terminatePath(17);
    }
}
