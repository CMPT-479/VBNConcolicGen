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
            Call.finalizeStore("vbn.examples.Test_11_Increment.x", x, 7);
            Call.pushSym("vbn.examples.Test_11_Increment.x", x);
            var3 = x;
            Call.finalizeStore("$i1", var3, 8);
            Call.pushSym("$i1", var3);
            Call.pushConstant(1);
            Call.apply(" + ");
            ++var3;
            Call.finalizeStore("$i2", var3, 8);
            Call.pushSym("$i2", var3);
            x = var3;
            Call.finalizeStore("vbn.examples.Test_11_Increment.x", x, 8);
            Call.terminatePath(9);
        } catch (Throwable var1) {
            Call.terminatedWithError(var1);
        }
    }

    @Test
    public void functionTest__07_DiffTypes() {
        Call.initTestingMode();
        String[] var0 = {"12", "123", "1234", "1234", "1234"};
        var x = 1;
        var y = 4;
        var z = 4;
        var t = true;

        try {
            Call.init();
            Call.popArg("r0", var0);
            Call.pushSym("r0[0]", var0[0]);
            String var1;
            String var10000 = var1 = var0[0];
            Call.finalizeStore("$r1", var1, 9);
            int var5 = Integer.parseInt(var10000);
            Call.popArg("$i0", var5);
            Call.finalizeReturn("$i0", var5, 9);
            Call.pushSym("$i0", var5);
            x = var5;
            Call.finalizeStore("vbn.examples.Test_07_If_Multiple_Diff_Types.x", x, 9);
            Call.pushSym("r0[1]", var0[1]);
            var10000 = var1 = var0[1];
            Call.finalizeStore("$r2", var1, 10);
            var5 = Integer.parseInt(var10000);
            Call.popArg("$i1", var5);
            Call.finalizeReturn("$i1", var5, 10);
            Call.pushSym("$i1", var5);
            y = var5;
            Call.finalizeStore("vbn.examples.Test_07_If_Multiple_Diff_Types.y", y, 10);
            Call.pushSym("r0[2]", var0[2]);
            var10000 = var1 = var0[2];
            Call.finalizeStore("$r3", var1, 11);
            var5 = Integer.parseInt(var10000);
            Call.popArg("$i2", var5);
            Call.finalizeReturn("$i2", var5, 11);
            Call.pushSym("$i2", var5);
            z = var5;
            Call.finalizeStore("vbn.examples.Test_07_If_Multiple_Diff_Types.z", z, 11);
            Call.pushSym("r0[3]", var0[3]);
            String var3;
            var10000 = var3 = var0[3];
            Call.finalizeStore("$r4", var3, 12);
            boolean var4 = Boolean.parseBoolean(var10000);
            Call.popArg("$z0", var4);
            Call.finalizeReturn("$z0", var4, 12);
            Call.pushSym("$z0", var4);
            t = var4;
            Call.finalizeStore("vbn.examples.Test_07_If_Multiple_Diff_Types.t", t, 12);
            Call.pushSym("vbn.examples.Test_07_If_Multiple_Diff_Types.t", t);
            var4 = t;
            Call.finalizeStore("$z1", var4, 13);
            Call.pushSym("$z1", var4);
            Call.pushConstant(0);
            Call.apply(" == ");
            if (var4) {
                Call.pushFalseBranch(13);
            } else {
                Call.pushTrueBranch(13);
            }

            Call.finalizeIf(13);
            PrintStream var7;
            PrintStream var8;
            if (var4) {
                Call.pushSym("vbn.examples.Test_07_If_Multiple_Diff_Types.x", x);
                var5 = x;
                Call.finalizeStore("$i4", var5, 14);
                Call.pushSym("vbn.examples.Test_07_If_Multiple_Diff_Types.y", y);
                int var6 = y;
                Call.finalizeStore("$i3", var6, 14);
                Call.pushSym("$i4", var5);
                Call.pushSym("$i3", var6);
                Call.apply(" <= ");
                if (var5 > var6) {
                    Call.pushFalseBranch(14);
                } else {
                    Call.pushTrueBranch(14);
                }

                Call.finalizeIf(14);
                if (var5 > var6) {
                    Call.pushSym("java.lang.System.out", System.out);
                    var8 = var7 = System.out;
                    Call.finalizeStore("$r9", var7, 15);
                    var8.println("Path 1");
                    Call.pushSym("vbn.examples.Test_07_If_Multiple_Diff_Types.y", y);
                    var5 = y;
                    Call.finalizeStore("$i8", var5, 16);
                    Call.pushSym("vbn.examples.Test_07_If_Multiple_Diff_Types.z", z);
                    var6 = z;
                    Call.finalizeStore("$i7", var6, 16);
                    Call.pushSym("$i8", var5);
                    Call.pushSym("$i7", var6);
                    Call.apply(" <= ");
                    if (var5 > var6) {
                        Call.pushFalseBranch(16);
                    } else {
                        Call.pushTrueBranch(16);
                    }

                    Call.finalizeIf(16);
                    if (var5 > var6) {
                        Call.pushSym("java.lang.System.out", System.out);
                        var8 = var7 = System.out;
                        Call.finalizeStore("$r11", var7, 17);
                        var8.println("Path 1.1");
                    } else {
                        Call.pushSym("java.lang.System.out", System.out);
                        var8 = var7 = System.out;
                        Call.finalizeStore("$r10", var7, 19);
                        var8.println("Path 1.2");
                    }
                } else {
                    Call.pushSym("java.lang.System.out", System.out);
                    var8 = var7 = System.out;
                    Call.finalizeStore("$r6", var7, 22);
                    var8.println("Path 2");
                    Call.pushSym("vbn.examples.Test_07_If_Multiple_Diff_Types.x", x);
                    var5 = x;
                    Call.finalizeStore("$i6", var5, 23);
                    Call.pushSym("vbn.examples.Test_07_If_Multiple_Diff_Types.z", z);
                    var6 = z;
                    Call.finalizeStore("$i5", var6, 23);
                    Call.pushSym("$i6", var5);
                    Call.pushSym("$i5", var6);
                    Call.apply(" <= ");
                    if (var5 > var6) {
                        Call.pushFalseBranch(23);
                    } else {
                        Call.pushTrueBranch(23);
                    }

                    Call.finalizeIf(23);
                    if (var5 > var6) {
                        Call.pushSym("java.lang.System.out", System.out);
                        var8 = var7 = System.out;
                        Call.finalizeStore("$r8", var7, 24);
                        var8.println("Path 2.1");
                    } else {
                        Call.pushSym("java.lang.System.out", System.out);
                        var8 = var7 = System.out;
                        Call.finalizeStore("$r7", var7, 26);
                        var8.println("Path 2.2");
                    }
                }
            } else {
                Call.pushSym("java.lang.System.out", System.out);
                var8 = var7 = System.out;
                Call.finalizeStore("$r5", var7, 30);
                var8.println("Path 3");
            }

            Call.terminatePath(33);
        } catch (Throwable var2) {
            Call.terminatedWithError(var2);
        }
    }

    @Test
    public void handleRecast() {
        Call.initTestingMode();
        String[] var0 = {"12", "123", "1234", "1234", "1234"};
        float x = 1;
        double y = 4;
        float z = 4;
        var t = true;

        try {
            Call.init();
            Call.popArg("r0", var0);
            Call.pushSym("r0[0]", var0[0]);
            String var4;
            String var10000 = var4 = var0[0];
            Call.finalizeStore("$r1", var4, 7);
            float var5 = Float.parseFloat(var10000);
            Call.popArg("$f0", var5);
            Call.finalizeReturn("$f0", var5, 7);
            Call.pushSym("$f0", var5);
            x = var5;
            Call.finalizeStore("vbn.examples.Test_10_If_Float.x", x, 7);
            Call.pushSym("vbn.examples.Test_10_If_Float.x", x);
            float var10 = x;
            Float var6 = var10;
            Call.finalizeStore("$f1", var6, 8);
            double var1 = (double)var10;
            Call.finalizeStore("$d0", var1, 8);
            Call.pushSym("$d0", var1);
            Call.pushConstant(0.1);
            Call.apply(" cmpl ");
            double var13;
            int var11 = (var13 = var1 - 0.1) == 0.0 ? 0 : (var13 < 0.0 ? -1 : 1);
            double var14;
            Byte var7 = Byte.valueOf((byte)((var14 = var1 - 0.1) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1)));
            Call.finalizeStore("$b0", var7, 8);
            int var8 = var11;
            Call.finalizeStore("$i1", var8, 135);
            Call.pushSym("$i1", var8);
            Call.pushConstant(0);
            Call.apply(" <= ");
            if (var8 > 0) {
                Call.pushFalseBranch(8);
            } else {
                Call.pushTrueBranch(8);
            }

            Call.finalizeIf(8);
            PrintStream var9;
            PrintStream var12;
            if (var8 > 0) {
                Call.pushSym("java.lang.System.out", System.out);
                var12 = var9 = System.out;
                Call.finalizeStore("$r3", var9, 9);
                var12.println("Less than 0.1");
            } else {
                Call.pushSym("java.lang.System.out", System.out);
                var12 = var9 = System.out;
                Call.finalizeStore("$r2", var9, 11);
                var12.println("Greater than 0.1");
            }

            Call.terminatePath(13);
        } catch (Throwable var3) {
            Call.terminatedWithError(var3);
        }
    }

    @Test
    public void testIncrement() {
        Call.initTestingMode();

        String[] var0 = {"12", "123", "1234", "1234", "1234"};
        int x = 1;

        try {
            Call.init();
            Call.popArg("r0", var0);
            Call.pushSym("r0[0]", var0[0]);
            String var2;
            String var10000 = var2 = var0[0];
            Call.finalizeStore("$r1", var2, 7);
            int var3 = Integer.parseInt(var10000);
            Call.pushSym("$i0", var3);
            Call.finalizeStore("$i0", var3, 7);
            Call.pushSym("$i0", var3);
            x = var3;
            Call.finalizeStore("vbn.examples.Test_11_Increment.x", x, 7);
            Call.pushSym("vbn.examples.Test_11_Increment.x", x);
            var3 = x;
            Call.finalizeStore("$i1", var3, 8);
            Call.pushSym("$i1", var3);
            Call.pushConstant(1);
            Call.apply(" + ");
            ++var3;
            Call.finalizeStore("$i2", var3, 8);
            Call.pushSym("$i2", var3);
            x = var3;
            Call.finalizeStore("vbn.examples.Test_11_Increment.x", x, 8);
            Call.terminatePath(9);
        } catch (Throwable var1) {
            Call.terminatedWithError(var1);
        }
    }

    @Test
    public void testLoops() {
        Call.initTestingMode();

        String[] var0 = {"12", "123", "1234", "1234", "1234"};
        int x = 1;

        try {
            Call.init();
            Call.popArg("r0", var0);
            Call.pushSym("r0[0]", var0[0]);
            String var3;
            String var10000 = var3 = var0[0];
            Call.finalizeStore("$r1", var3, 7);
            int var4 = Integer.parseInt(var10000);
            Call.pushSym("$i0", var4);
            Call.finalizeStore("$i0", var4, 7);
            Call.pushSym("$i0", var4);
            x = var4;
            Call.finalizeStore("vbn.examples.Test_13_Loop.x", x, 7);
            Call.pushSym("vbn.examples.Test_13_Loop.x", x);
            var4 = x;
            Call.finalizeStore("$i1", var4, 8);
            Call.pushSym("$i1", var4);
            Call.pushConstant(1);
            Call.apply(" + ");
            ++var4;
            Call.finalizeStore("i2", var4, 8);
            Call.pushSym("vbn.examples.Test_13_Loop.x", x);
            int var1 = x;
            Call.finalizeStore("i4", var1, 11);

            while(true) {
                Call.pushSym("i4", var1);
                Call.pushSym("i2", var4);
                Call.apply(" >= ");
                if (var1 < var4) {
                    Call.pushFalseBranch(11);
                } else {
                    Call.pushTrueBranch(11);
                }

                Call.finalizeIf(11);
                if (var1 >= var4) {
                    Call.pushSym("java.lang.System.out", System.out);
                    PrintStream var5;
                    PrintStream var7 = var5 = System.out;
                    Call.finalizeStore("$r2", var5, 17);
                    Call.pushSym("vbn.examples.Test_13_Loop.x", x);
                    int var10001 = x;
                    Integer var6 = var10001;
                    Call.finalizeStore("$i3", var6, 17);
                    var7.println(var10001);
                    Call.terminatePath(18);
                    break;
                }

                Call.pushSym("i4", var1);
                Call.pushConstant(1);
                Call.apply(" + ");
                ++var1;
                Call.finalizeStore("i4", var1, 11);
            }
        } catch (Throwable var2) {
//            Call.terminatedWithError(var2);
            throw var2;
        }

        Call.initTestingMode();
        var constraints = Call.getConstraints();

        System.out.println("===========================================================");
        for (var constraint : constraints) {
            System.out.println(constraint);
        }
    }
}
