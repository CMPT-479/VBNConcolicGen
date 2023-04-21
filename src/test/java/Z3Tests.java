import org.junit.jupiter.api.Test;
import vbn.Call;
import vbn.solver.VBNRunner;
import vbn.solver.Z3Solver;
import vbn.state.State;
import vbn.state.constraints.BinaryConstraint;
import vbn.state.constraints.BinaryOperand;
import vbn.state.value.ISymbol;
import vbn.state.value.BooleanSymbol;
import vbn.state.value.IntSymbol;

import java.io.PrintStream;
import java.util.List;

public class Z3Tests {
    @Test
    final void testZ3SimpleOr() {
        // x or y
        State state = new State();
        state.addSymbol(new BooleanSymbol("x", false));  // concrete value doesn't matter
        state.addSymbol(new BooleanSymbol("y", false));  // concrete value doesn't matter
        state.pushConstraint(
                new BinaryConstraint(
                        state.getSymbol("x"),
                        BinaryOperand.OR,
                        state.getSymbol("y")));

        List<ISymbol> solved = Z3Solver.solve(state);
        Z3Solver.printSolvedValuesBasedOnList(solved);
    }

    @Test
    final void testZ3SimpleAnd() {
        // x and y
        State state = new State();
        state.addSymbol(new BooleanSymbol("x", false));  // concrete value doesn't matter
        state.addSymbol(new BooleanSymbol("y", false));  // concrete value doesn't matter
        state.pushConstraint(
                new BinaryConstraint(
                        state.getSymbol("x"),
                        BinaryOperand.AND,
                        state.getSymbol("y")));

        List<ISymbol> solved = Z3Solver.solve(state);
        Z3Solver.printSolvedValuesBasedOnList(solved);
    }

    @Test
    final void testZ3Gt() {
        // x > y
        State state = new State();
        state.addSymbol(new IntSymbol("x", 1));  // concrete value doesn't matter
        state.addSymbol(new IntSymbol("y", 1));  // concrete value doesn't matter
        state.pushConstraint(
                new BinaryConstraint(
                        state.getSymbol("x"),
                        BinaryOperand.GT,
                        state.getSymbol("y")));

        List<ISymbol> solved = Z3Solver.solve(state);
        Z3Solver.printSolvedValuesBasedOnList(solved);
    }

    @Test
    final void testZ3If_Multiple_Diff_Types() {
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

        // x > y
        State state = VBNRunner.returnStateFromIO();

        List<ISymbol> solved = Z3Solver.solve(state);
        Z3Solver.printSolvedValuesBasedOnList(solved);
    }
}
