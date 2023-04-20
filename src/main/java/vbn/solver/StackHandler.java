package vbn.solver;

import lombok.NonNull;
import vbn.constraints.Constraint;
import vbn.constraints.State;

import java.util.Stack;

public class StackHandler {
    public static boolean compareConstraintStacks(@NonNull Stack<Constraint> stack1, @NonNull Stack<Constraint> stack2) {
        if (stack1.size() != stack2.size()) {
            return false;
        }

        int stackSizes = stack1.size();
        for (int i = 0; i < stackSizes; i++) {
            if (stack1.get(i) != stack2.get(i)) {
                return false;
            }
        }

        return true;
    }

//    public static State
}
