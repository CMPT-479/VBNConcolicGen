package vbn.solver;

import lombok.NonNull;
import vbn.ObjectIO;
import vbn.RandomHandler;
import vbn.state.constraints.IConstraint;
import vbn.state.State;
import vbn.state.value.*;

import java.util.*;

public class VBNRunner {

    private static final String fileNameSymbols = "stateSymbols.ser";
    private static final String fileNameConstraints = "stateConstraints.ser";

    // false represents not negated, true negated
    // these keep track of our personally negated branches when we try to explore every branch
    public static Map<Integer, Boolean> constraintNegatedMap = new HashMap<>();

    // this is the original negations upon the initial run (if we go down a false branch)
    public static Map<Integer, Boolean> constraintOriginallyNegated = new HashMap<>();
    public static List<String[]> solvedConstraints = new ArrayList<>();

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

    private static String[] getProgramInputs(@NonNull List<IConstant> constants) {
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
                    throw new VBNSolverRuntimeError("Unknown type as program input");
            }
        }

        return inputs;
    }

    private static String[] abstractSymbolListToStringArray(List<ISymbol> abstractSymbolList, boolean printable) {
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

    private static void putInitialConstraintPathDirection(Stack<IConstraint> constraints) {
        for (IConstraint constraint : constraints) {
            constraintOriginallyNegated.put(constraint.getLineNumber(), constraint.getOriginalEvaluation());
        }
    }

    @NonNull
    private static Stack<IConstraint> removeInvalidConstraints(@NonNull Stack<IConstraint> constraints) {
        Stack<IConstraint> validConstraints = new Stack<>();
        for (IConstraint constraint : constraints) {
            if (constraint.hasLineNumber()) {
                validConstraints.push(constraint);
            }
        }
        return validConstraints;
    }

    /**
     *
     * @param constraints
     * @return boolean representing if we were able to negate successfully, if false, unsuccessful in negating
     */
    private static boolean negateConstraints(Stack<IConstraint> constraints) {
        printConstraintNegationStatus();

        while (!(constraints.empty())) {
            int lineNumber = constraints.peek().getLineNumber();
            if (!constraintNegatedMap.containsKey(lineNumber)) {
                throw new RuntimeException("Constraint negated map did not contain the constraint line number");
            }

            if (!constraintNegatedMap.get(lineNumber)) {
                // map contains the key
                // the top has a line number (is negateable)
                // the top is not negated
                constraintNegatedMap.put(lineNumber, true);
                return true;
            } else {
                constraints.pop();
            }
        }
        return false;
    }

    public static int execute(String programName) {
        // programInputs shouldn't be necessary, we should be able to generate these automatically the first time
        final String[] args = new String[] {programName};
        soot.Main.main(args);
        @NonNull List<IConstant> programInputTypes = InputMap.programInputMap.get(programName);
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
        constraints = removeInvalidConstraints(constraints);
        putInitialConstraintPathDirection(constraints);

        while (!(constraints.empty())) {
            constraints = removeInvalidConstraints(constraints);
            boolean negationResults = negateConstraints(constraints);
            if (!negationResults) {
                System.out.println("No more constraints to negate");
                return 0;
            }

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
            if (!constraint.hasLineNumber()) {
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
