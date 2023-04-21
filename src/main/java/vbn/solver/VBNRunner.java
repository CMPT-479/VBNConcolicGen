package vbn.solver;

import lombok.NonNull;
import vbn.ObjectIO;
import vbn.RandomHandler;
import vbn.state.constraints.AbstractConstraint;
import vbn.state.State;
import vbn.state.value.AbstractConstant;
import vbn.state.value.AbstractSymbol;
import vbn.state.value.BooleanConstant;
import vbn.state.value.Value;

import java.util.*;

public class VBNRunner {

    static String fileNameSymbols = "stateSymbols.ser";
    static String fileNameConstraints = "stateConstraints.ser";

    public static void insertStateIntoIO(State state) {
        ObjectIO.writeObjectToFile(state.getSymbols(), fileNameSymbols);
        ObjectIO.writeObjectToFile(state.getConstraints(), fileNameConstraints);
    }

    public static State returnStateFromIO() {
        @NonNull ArrayList<AbstractSymbol> symbols = (ArrayList<AbstractSymbol>) ObjectIO.readObjectFromFile(fileNameSymbols);
        @NonNull Stack<AbstractConstraint> constraints = (Stack<AbstractConstraint>) ObjectIO.readObjectFromFile(fileNameConstraints);

        // convert symbol arrayList to symbol map
        Map<String, AbstractSymbol> symbolMap = new HashMap<>();
        for (AbstractSymbol symbol : symbols) {
            symbolMap.put(symbol.getName(), symbol);
        }

        ObjectIO.deleteFile(fileNameSymbols);
        ObjectIO.deleteFile(fileNameConstraints);

        return new State(symbolMap, constraints);
    }

    static Map<String, List<AbstractConstant>> programInputMap = Map.ofEntries(
            Map.entry("vbn.examples.Test_00_Basic", List.of(new BooleanConstant(false), new BooleanConstant(false)))
    );

    static String[] getProgramInputs(List<AbstractConstant> constants) {
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

    public static int execute(String programName) {
        // programInputs shouldn't be necessary, we should be able to generate these automatically the first time
        final String[] args = new String[] {programName};
        soot.Main.main(args);
        @NonNull List<AbstractConstant> programInputTypes = programInputMap.get(programName);
        var exitCode = InstrumentedRunner.runInstrumented(programName, getProgramInputs(programInputTypes));

        Stack<AbstractConstraint> stateConstraint;
//        while (true) {
            // this global state needs to be obtained from an external data store
//            State state = new State(); // shouldn't be a "new"
//            ArrayList<AbstractConstant> solved = Z3Solver.solve(state);   // need to save these solved values somewhere
//            stateConstraint = state.getConstraints();
//            while (!(stateConstraint.empty()) && (stateConstraint.peek().negated)) {
//                stateConstraint.pop();
//            }
//            if (stateConstraint.empty()) {
//                break;
//            }
//            stateConstraint.peek().negated = true;
//            InstrumentedRunner.runInstrumented(programName, programInputs);
//        }

        return exitCode;
    }
}
