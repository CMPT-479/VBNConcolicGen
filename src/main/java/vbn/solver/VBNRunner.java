package vbn.solver;

import vbn.state.constraints.AbstractConstraint;
import vbn.state.State;
import vbn.state.value.AbstractConstant;
import vbn.state.value.AbstractSymbol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class VBNRunner {
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
