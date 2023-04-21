package vbn.solver;

import lombok.NonNull;
import vbn.state.constraints.BinaryConstraint;
import vbn.state.constraints.IConstraint;
import vbn.state.constraints.UnaryConstraint;

import java.util.Stack;

public class StackHandler {
    public static boolean compareConstraintStacks(@NonNull Stack<IConstraint> stack1, @NonNull Stack<IConstraint> stack2) {
        if (stack1.size() != stack2.size()) {
            return false;
        }

        IConstraint s1Constraint;
        IConstraint s2Constraint;
        BinaryConstraint s1BinaryConstraint;
        BinaryConstraint s2BinaryConstraint;
        UnaryConstraint s1UnaryConstraint;
        UnaryConstraint s2UnaryConstraint;
        int stackSizes = stack1.size();
        for (int i = 0; i < stackSizes; i++) {
            s1Constraint = stack1.get(i);
            s2Constraint = stack2.get(i);
            if (!(s1Constraint.getClass().getName().equals(s2Constraint.getClass().getName()))) {
                return false;
            }

            if (s1Constraint instanceof BinaryConstraint) {
                s1BinaryConstraint = (BinaryConstraint) s1Constraint;
                s2BinaryConstraint = (BinaryConstraint) s2Constraint;
                if (!(s1BinaryConstraint.equals(s2BinaryConstraint))) {
                    return false;
                }

            } else if (s1Constraint instanceof UnaryConstraint) {
                s1UnaryConstraint = (UnaryConstraint) s1Constraint;
                s2UnaryConstraint = (UnaryConstraint) s2Constraint;
                if (!(s1UnaryConstraint.equals(s2UnaryConstraint))) {
                    return false;
                }

            } else {
                throw new RuntimeException("Error, constraint not a type of a class we expect");
            }
        }

        return true;
    }

    public static boolean visitedPreviouslySeenPath(Stack<IConstraint> stack) {
        return false;
    }

//    public static State
}
