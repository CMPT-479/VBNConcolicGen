package vbn.solver;

import vbn.state.value.BooleanConstant;
import vbn.state.value.IConstant;
import vbn.state.value.IntConstant;
import vbn.state.value.RealConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InputMap {
    // input map constants
    private static final int DEFAULT_INT_CONSTANT = -123456; // for instances where we don't care about the int constant
    private static final double DEFAULT_REAL_CONSTANT = -123456.0; // for instances where we don't care about the real constant
    private static final boolean DEFAULT_BOOL_CONSTANT = false; // for instances where we don't care about the bool constant

    public static Map<String, List<IConstant>> programInputMap = Map.ofEntries(
            Map.entry(
                    "vbn.examples.Test_00_Basic",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.examples.Test_02_NEG_vs_MINUS",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new BooleanConstant(DEFAULT_BOOL_CONSTANT))),
            Map.entry(
                    "vbn.examples.Test_05_If",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.examples.Test_06_If_Multiple",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.examples.Test_07_If_Multiple_Diff_Types",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new BooleanConstant(DEFAULT_BOOL_CONSTANT))),
            Map.entry(
                    "vbn.examples.Test_08_If_Value",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.examples.Test_09_If_Double",
                    List.of(
                            new RealConstant(DEFAULT_REAL_CONSTANT))),
            Map.entry(
                    "vbn.examples.Test_10_If_Float",
                    List.of(
                            new RealConstant(DEFAULT_REAL_CONSTANT))),
            Map.entry(
                    "vbn.examples.Test_11_Unmanageable",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.examples.Test_12_Increment",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.examples.Test_13_Function",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.examples.Test_14_Loop",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.ksen_tests.If",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.ksen_tests.Linear",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.ksen_tests.Nonlinear",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT)))
    );
}
