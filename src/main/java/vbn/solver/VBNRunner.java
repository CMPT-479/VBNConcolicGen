package vbn.solver;

import vbn.state.constraints.Constraint;
import vbn.state.State;
import vbn.state.value.AbstractSymbolConstant;
import vbn.state.value.Symbol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class VBNRunner {
    public static void execute(String programName, String[] programInputs) {
        // programInputs shouldn't be necessary, we should be able to generate these automatically the first time
        final String[] args = new String[] {programName};
        soot.Main.main(args);
        InstrumentedRunner.runInstrumented(programName, programInputs);
        while (true) {
            // this global state needs to be obtained from an external data store
            State state = new State(); // shouldn't be a "new"
            ArrayList<AbstractSymbolConstant> solved = Z3Solver.solve(state);
            Stack<Constraint> stateConstraint = state.getConstraints();
            while (!(stateConstraint.empty()) && (stateConstraint.peek().negated)) {
                stateConstraint.pop();
            }
            if (stateConstraint.empty()) {
                break;
            }
            stateConstraint.peek().negated = true;

            // negate last item in stack or pop if negated last item already, then negate
            // while ()

            InstrumentedRunner.runInstrumented(programName, programInputs);
        }
    }
}
