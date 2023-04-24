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
                    "vbn.examples.Test_01_Array",
                    new ArrayList<>()),
            Map.entry(
                    "vbn.examples.Test_02_NEG_vs_MINUS",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new BooleanConstant(DEFAULT_BOOL_CONSTANT))),
            Map.entry(
                    "vbn.examples.Test_03_Class",
                    new ArrayList<>()),
            Map.entry(
                    "vbn.examples.Test_04_Cast",
                    new ArrayList<>()),
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
                    "vbn.examples.Test_11_Increment",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.examples.Test_12_Function",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.examples.Test_13_Loop",
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
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.ksen_tests.Unmanageable",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.ksen_tests.Testme",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.ksen_tests.InterfaceTest",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.crest_tests.Unary",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.crest_tests.Math",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.crest_tests.Simple",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.crest_tests.UniformTest",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.crest_tests.Structure",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT),
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.crest_tests.Function",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT))),
            Map.entry(
                    "vbn.crest_tests.CFG",
                    List.of(
                            new IntConstant(DEFAULT_INT_CONSTANT)))
    );
}
