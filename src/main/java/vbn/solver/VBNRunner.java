package vbn.solver;

import lombok.NonNull;
import vbn.ObjectIO;
import vbn.RandomHandler;
import vbn.state.constraints.IConstraint;
import vbn.state.State;
import vbn.state.value.*;

import java.util.*;

public class VBNRunner {

    static String fileNameSymbols = "stateSymbols.ser";
    static String fileNameConstraints = "stateConstraints.ser";

    public static void insertStateIntoIO(State state) {
        ObjectIO.writeObjectToFile(state.getSymbols(), fileNameSymbols);
        ObjectIO.writeObjectToFile(state.getConstraints(), fileNameConstraints);
    }

    public static State returnStateFromIO() {
        @NonNull ArrayList<ISymbol> symbols = (ArrayList<ISymbol>) ObjectIO.readObjectFromFile(fileNameSymbols);
        @NonNull Stack<IConstraint> constraints = (Stack<IConstraint>) ObjectIO.readObjectFromFile(fileNameConstraints);

        // convert symbol arrayList to symbol map
        Map<String, ISymbol> symbolMap = new HashMap<>();
        for (ISymbol symbol : symbols) {
            symbolMap.put(symbol.getName(), symbol);
        }

        ObjectIO.deleteFile(fileNameSymbols);
        ObjectIO.deleteFile(fileNameConstraints);

        return new State(symbolMap, constraints);
    }

    // input map constants
    static int DEFAULT_INT_CONSTANT = -123456; // for instances where we don't care about the int constant
    static double DEFAULT_REAL_CONSTANT = -123456.0; // for instances where we don't care about the real constant
    static boolean DEFAULT_BOOL_CONSTANT = false; // for instances where we don't care about the bool constant

    static Map<String, List<IConstant>> programInputMap = Map.ofEntries(
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
                            new BooleanConstant(DEFAULT_BOOL_CONSTANT)))
    );

    static String[] getProgramInputs(@NonNull List<IConstant> constants) {
        String[] inputs = new String[constants.size()];
        for (int i = 0; i < constants.size(); i++) {
            Value.Type type = constants.get(i).getType();
            switch (type) {
                case INT_TYPE:
                    inputs[i] = String.valueOf(RandomHandler.getRandomNumber());
                    break;
                case REAL_TYPE:
                    inputs[i] = String.valueOf(RandomHandler.getRandomReal());
                    break;
                case BOOL_TYPE:
                    inputs[i] = String.valueOf(RandomHandler.getRandomBoolean());
                    break;
                case UNKNOWN:
                    throw new RuntimeException("Unknown type as program input");
            }
        }

        return inputs;
    }

    static String[] abstractSymbolListToStringArray(List<ISymbol> abstractSymbolList, boolean printable) {
        String[] abstractSymbolArray = new String[abstractSymbolList.size()];
        for (int i = 0; i < abstractSymbolList.size(); i++) {
            if (!printable) {
                abstractSymbolArray[i] = abstractSymbolList.get(i).getValue().toString();
            } else {
                abstractSymbolArray[i] = abstractSymbolList.get(i).getName() + " = " + abstractSymbolList.get(i).getValue().toString();
            }
        }
        return abstractSymbolArray;
    }

    public static Map<Integer, Boolean> constraintNegatedMap = new HashMap<>();   // false represents not negated, true negated
    public static List<String[]> solvedConstraints = new ArrayList<>();

    public static int execute(String programName) {
        // programInputs shouldn't be necessary, we should be able to generate these automatically the first time
        final String[] args = new String[] {programName};
        soot.Main.main(args);
        @NonNull List<IConstant> programInputTypes = programInputMap.get(programName);
        String[] programInputs = getProgramInputs(programInputTypes);
        solvedConstraints.add(programInputs);
        // Step 1: Run program on random inputs
        var exitCode = InstrumentedRunner.runInstrumented(programName, programInputs);
        if (exitCode != 0) {
            return exitCode;
        }

        @NonNull State state = returnStateFromIO();
        ArrayList<ISymbol> solved;

        @NonNull Stack<IConstraint> constraints = state.getConstraints();
        addConstraintsToNegatedMap(constraints);

        while (!(constraints.empty())) {
            // this global state needs to be obtained from an external data store
            // solved = Z3Solver.solve(state);   // need to save these solved values somewhere
            int linenumber = -1;
            while (!(constraints.empty())) {
                linenumber = constraints.peek().getLineNumber();
                if (linenumber == -1) {
                    constraints.pop();
                    continue;
                }
                if (constraintNegatedMap.containsKey(linenumber) && !constraintNegatedMap.get(linenumber)) {
                    // map contains the key
                    // the top has a line number (is negateable)
                    // the top is not negated
                    break;
                } else {
                    constraints.pop();
                    continue;
                }
            }
            if (constraints.empty()) {
                System.out.println("No more constraints to negate");
                return 0;
            }

            printConstraintNegationStatus();
            constraintNegatedMap.put(linenumber, true);
            solved = Z3Solver.solve(state); // solve for negated end
            programInputs = abstractSymbolListToStringArray(solved, false);
            solvedConstraints.add(programInputs);

            // printable for debugging
            System.out.println("Solved " + Arrays.toString(abstractSymbolListToStringArray(solved, true)));
            // Step 2: Run program on negated inputs
            exitCode = InstrumentedRunner.runInstrumented(programName, programInputs);
            if (exitCode != 0) {
                return exitCode;
            }

            state = returnStateFromIO();
            constraints = state.getConstraints();
            addConstraintsToNegatedMap(constraints);
        }

        return 0;
    }

    private static void addConstraintsToNegatedMap(@NonNull Stack<IConstraint> constraints) {
        for (@NonNull IConstraint constraint : constraints) {
            if (constraint.getLineNumber() == -1) {
                continue;
            }
            if (!(constraintNegatedMap.containsKey(constraint.getLineNumber()))) {
                constraintNegatedMap.put(constraint.getLineNumber(), false);
            }
        }
    }

    public static void printSolvedConstraints() {
        int i = 0;
        for (String[] s : solvedConstraints) {
            System.out.println("Solved input (" + i + "): " + Arrays.toString(s));
            i++;
        }
    }

    public static void printConstraintNegationStatus() {
        for (Map.Entry<Integer, Boolean> entry : constraintNegatedMap.entrySet()) {
            System.out.print(entry.getKey() + ": " + entry.getValue() + ", ");
            System.out.println();
        }
    }
}
