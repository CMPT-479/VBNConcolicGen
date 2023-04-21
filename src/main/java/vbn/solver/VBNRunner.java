package vbn.solver;

import lombok.NonNull;
import vbn.ObjectIO;
import vbn.state.constraints.AbstractConstraint;
import vbn.state.State;
import vbn.state.value.AbstractConstant;
import vbn.state.value.AbstractSymbol;

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

    public static void execute(String programName, String[] programInputs) {
        // programInputs shouldn't be necessary, we should be able to generate these automatically the first time
        final String[] args = new String[] {programName};
        soot.Main.main(args);
        InstrumentedRunner.runInstrumented(programName, programInputs);

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
    }
}
