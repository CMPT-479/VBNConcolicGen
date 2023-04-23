package vbn.solver;

import lombok.NonNull;
import vbn.ObjectIO;
import vbn.RandomHandler;
import vbn.state.constraints.IConstraint;
import vbn.state.GlobalState;
import vbn.state.value.*;

import java.util.*;

public class VBNRunner {

    private static final String fileNameSymbols = "stateSymbols.ser";
    private static final String fileNameConstraints = "stateConstraints.ser";

    // false represents not negated, true negated
    // these keep track of our personally negated branches when we try to explore every branch
    public static Map<Long, Boolean> constraintNegatedMap = new HashMap<Long, Boolean>();

    // this is the original negations upon the initial run (if we go down a false branch)
    public static Map<Long, Boolean> constraintOriginallyNegated = new HashMap<Long, Boolean>();
    public static List<String[]> solvedConstraints = new ArrayList<>();

    public static void reset() {
        constraintNegatedMap.clear();
        constraintOriginallyNegated.clear();
        solvedConstraints.clear();
    }

    public static void insertStateIntoIO(GlobalState globalState) {
        ObjectIO.writeObjectToFile(globalState.getSymbols(), fileNameSymbols);
        ObjectIO.writeObjectToFile(globalState.getConstraints(), fileNameConstraints);
    }

    public static GlobalState returnStateFromIO() {
        @NonNull ArrayList<ISymbol> symbols = (ArrayList<ISymbol>) ObjectIO.readObjectFromFile(fileNameSymbols);
        @NonNull Stack<IConstraint> constraints = (Stack<IConstraint>) ObjectIO.readObjectFromFile(fileNameConstraints);

        // convert symbol arrayList to symbol map
        Map<String, ISymbol> symbolMap = new HashMap<>();
        for (ISymbol symbol : symbols) {
            symbolMap.put(symbol.getName(), symbol);
        }

        ObjectIO.deleteFile(fileNameSymbols);
        ObjectIO.deleteFile(fileNameConstraints);

        GlobalState returnGlobalState = new GlobalState(symbolMap, constraints);
        checkStateError(returnGlobalState);
        return returnGlobalState;
    }

    private static String[] getProgramInputs(@NonNull List<IConstant> constants, long randSeed) {
        RandomHandler randomHandler = new RandomHandler(randSeed);
        String[] inputs = new String[constants.size()];
        for (int i = 0; i < constants.size(); i++) {
            Value.Type type = constants.get(i).getType();
            switch (type) {
                case INT_TYPE:
                    inputs[i] = String.valueOf(randomHandler.getRandomNumber());
                    break;
                case REAL_TYPE:
                    inputs[i] = String.valueOf(randomHandler.getRandomReal());
                    break;
                case BOOL_TYPE:
                    inputs[i] = String.valueOf(randomHandler.getRandomBoolean());
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
            constraintOriginallyNegated.put(constraint.getUniqueId(), !constraint.getOriginalEvaluation());
        }
    }

    @NonNull
    private static Stack<IConstraint> removeInvalidConstraints(@NonNull Stack<IConstraint> constraints) {
        Stack<IConstraint> validConstraints = new Stack<>();
        for (IConstraint constraint : constraints) {
            if (constraint.isBranch()) {
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
            long lineNumber = constraints.peek().getUniqueId();
            if (!constraintNegatedMap.containsKey(lineNumber)) {
                throw new VBNSolverRuntimeError("Constraint negated map did not contain the constraint line number");
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

    private static void checkStateError(GlobalState globalState) {
        if (globalState.getError() == null) {
            return;
        }

        if (globalState.hasVBNError()) {
            throw new VBNSolverRuntimeError(globalState.getError());
        } else {
            globalState.getError().printStackTrace();
        }
    }

    public static void execute(String programName) {
        execute(programName, System.currentTimeMillis());
    }

    public static void execute(String programName, long randomSeed) {
        // programInputs shouldn't be necessary, we should be able to generate these automatically the first time
        final String[] args = new String[] {programName};
        soot.Main.main(args);
        @NonNull List<IConstant> programInputTypes = InputMap.programInputMap.get(programName);
        String[] programInputs = getProgramInputs(programInputTypes, randomSeed);
        solvedConstraints.add(programInputs);
        // Step 1: Run program on random inputs
        InstrumentedRunner.runInstrumented(programName, programInputs);
        @NonNull GlobalState globalState = returnStateFromIO();
        @NonNull Stack<IConstraint> constraints = globalState.getConstraints();
        addConstraintsToNegatedMap(constraints);
        constraints = removeInvalidConstraints(constraints);
        putInitialConstraintPathDirection(constraints);

        ArrayList<ISymbol> solved;
        Z3Solver.solve(globalState);

        while (!(constraints.empty())) {
            constraints = removeInvalidConstraints(constraints);
            boolean negationResults = negateConstraints(constraints);
            if (!negationResults) {
                System.out.println("No more constraints to negate");
                break;
            }

            solved = Z3Solver.solve(globalState); // solve for negated end
            programInputs = abstractSymbolListToStringArray(solved, false);
            solvedConstraints.add(programInputs);

            // printable for debugging
            System.out.println("Solved " + Arrays.toString(abstractSymbolListToStringArray(solved, true)));
            // Step 2: Run program on negated inputs
            InstrumentedRunner.runInstrumented(programName, programInputs);
            globalState = returnStateFromIO();
            constraints = globalState.getConstraints();
            addConstraintsToNegatedMap(constraints);
        }
    }

    private static void addConstraintsToNegatedMap(@NonNull Stack<IConstraint> constraints) {
        for (IConstraint constraint : constraints) {
            if (constraint == null) {
                continue;
            }
            if (!constraint.isBranch()) {
                continue;
            }
            if (!(constraintNegatedMap.containsKey(constraint.getUniqueId()))) {
                constraintNegatedMap.put(constraint.getUniqueId(), false);
            }
        }
    }

    public static boolean printAndValidateSolvedConstraints(int expectedNumberOfSolutions) {
        printSolvedConstraints();
        if (expectedNumberOfSolutions != solvedConstraints.size()) {
            throw new VBNSolverRuntimeError("Expected " + expectedNumberOfSolutions + " solutions, but got " + solvedConstraints.size());
        }
        return true;
    }

    public static void printSolvedConstraints() {
        int i = 0;
        for (String[] s : solvedConstraints) {
            System.out.println("Solved input (" + i + "): " + Arrays.toString(s));
            i++;
        }
    }

    public static void printConstraintNegationStatus() {
        System.out.println("STARTING PRINT OF INITIAL CONSTRAINT MAP");
        for (Map.Entry<Long, Boolean> entry : constraintOriginallyNegated.entrySet()) {
            System.out.print(entry.getKey() + ": " + entry.getValue() + ", ");
            System.out.println();
        }
        System.out.println("STARTING PRINT OF CONSTRAINT NEGATION MAP");
        for (Map.Entry<Long, Boolean> entry : constraintNegatedMap.entrySet()) {
            System.out.print(entry.getKey() + ": " + entry.getValue() + ", ");
            System.out.println();
        }
    }
}
