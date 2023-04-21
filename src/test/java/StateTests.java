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
    @Test
    public void IfMultipleTest() {
        try {
            String[] var0 = {"12", "123", "1234", "false"};
            var x = 1;
            var y = 1;
            var z = 1;
            var t = true;

            Call.init();
            Call.pushSym("r0[0]", var0[0]);
            String var1;
            String var10000 = var1 = var0[0];
            Call.finalizeStore("$r1", var1);
            int var10 = Integer.parseInt(var10000);
            Integer var6 = var10;
            Call.pushSym("$i0", var6);
            x = var10;
            Call.finalizeStore("vbn.examples.Test_06_If_Multiple.x", x);
            Call.pushSym("r0[1]", var0[1]);
            var10000 = var1 = var0[1];
            Call.finalizeStore("$r2", var1);
            var10 = Integer.parseInt(var10000);
            var6 = var10;
            Call.pushSym("$i1", var6);
            y = var10;
            Call.finalizeStore("vbn.examples.Test_06_If_Multiple.y", y);
            Call.pushSym("r0[2]", var0[2]);
            var10000 = var1 = var0[2];
            Call.finalizeStore("$r3", var1);
            var10 = Integer.parseInt(var10000);
            var6 = var10;
            Call.pushSym("$i2", var6);
            z = var10;
            Call.finalizeStore("vbn.examples.Test_06_If_Multiple.z", z);
            Call.pushSym("r0[3]", var0[3]);
            String var3;
            var10000 = var3 = var0[3];
            Call.finalizeStore("$r4", var3);
            boolean var11 = Boolean.parseBoolean(var10000);
            Boolean var4 = var11;
            Call.pushSym("$z0", var4);
            t = var11;
            Call.finalizeStore("vbn.examples.Test_06_If_Multiple.t", t);
            Call.pushSym("vbn.examples.Test_06_If_Multiple.t", t);
            boolean var5 = t;
            Call.finalizeStore("$z1", var5);
            Call.pushSym("$z1", var5);
            Call.pushConstant(0);
            Call.apply(" == ");
            Call.finalizeIf(13);
            PrintStream var8;
            PrintStream var12;
            if (var5) {
                Call.pushSym("vbn.examples.Test_06_If_Multiple.x", x);
                int var9 = x;
                Call.finalizeStore("$i4", var9);
                Call.pushSym("vbn.examples.Test_06_If_Multiple.y", y);
                int var7 = y;
                Call.finalizeStore("$i3", var7);
                Call.pushSym("$i4", var9);
                Call.pushSym("$i3", var7);
                Call.apply(" <= ");
                Call.finalizeIf(14);
                if (var9 > var7) {
                    Call.pushSym("java.lang.System.out", System.out);
                    var12 = var8 = System.out;
                    Call.finalizeStore("$r9", var8);
                    var12.println("Path 1");
                    Call.pushSym("vbn.examples.Test_06_If_Multiple.y", y);
                    var9 = y;
                    Call.finalizeStore("$i8", var9);
                    Call.pushSym("vbn.examples.Test_06_If_Multiple.z", z);
                    var7 = z;
                    Call.finalizeStore("$i7", var7);
                    Call.pushSym("$i8", var9);
                    Call.pushSym("$i7", var7);
                    Call.apply(" <= ");
                    Call.finalizeIf(16);
                    if (var9 > var7) {
                        Call.pushSym("java.lang.System.out", System.out);
                        var12 = var8 = System.out;
                        Call.finalizeStore("$r11", var8);
                        var12.println("Path 1.1");
                    } else {
                        Call.pushSym("java.lang.System.out", System.out);
                        var12 = var8 = System.out;
                        Call.finalizeStore("$r10", var8);
                        var12.println("Path 1.2");
                    }
                } else {
                    Call.pushSym("java.lang.System.out", System.out);
                    var12 = var8 = System.out;
                    Call.finalizeStore("$r6", var8);
                    var12.println("Path 2");
                    Call.pushSym("vbn.examples.Test_06_If_Multiple.x", x);
                    var9 = x;
                    Call.finalizeStore("$i6", var9);
                    Call.pushSym("vbn.examples.Test_06_If_Multiple.z", z);
                    var7 = z;
                    Call.finalizeStore("$i5", var7);
                    Call.pushSym("$i6", var9);
                    Call.pushSym("$i5", var7);
                    Call.apply(" <= ");
                    Call.finalizeIf(23);
                    if (var9 > var7) {
                        Call.pushSym("java.lang.System.out", System.out);
                        var12 = var8 = System.out;
                        Call.finalizeStore("$r8", var8);
                        var12.println("Path 2.1");
                    } else {
                        Call.pushSym("java.lang.System.out", System.out);
                        var12 = var8 = System.out;
                        Call.finalizeStore("$r7", var8);
                        var12.println("Path 2.2");
                    }
                }
            } else {
                Call.pushSym("java.lang.System.out", System.out);
                var12 = var8 = System.out;
                Call.finalizeStore("$r5", var8);
                var12.println("Path 3");
            }

            Call.terminatePath(33);
        } catch (Throwable var2) {
            Call.terminatedWithError(var2);
        }
    }
}
