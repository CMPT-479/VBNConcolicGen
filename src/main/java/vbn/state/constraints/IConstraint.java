package vbn.state.constraints;

import java.io.Serializable;

public interface IConstraint extends Serializable {

    boolean hasLineNumber();

    /**
     * Get the "line number" where the constraint was in Jimple
     * (Implemented manually in instrumentation using a counter)
     * @return the "line number"
     */
    int getLineNumber();

    /**
     * Set the line number
     * @param lineNumber a unique id per constraint
     */
    void setLineNumber(int lineNumber);

    /**
     * Currently prints
     */
    void print();

    /**
     * Get the value of the expression, whether it is true or false
     * @return whether constraint evaluated to true
     */
    boolean getOriginalEvaluation();
}
