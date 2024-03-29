package vbn.state.constraints;

import lombok.NonNull;
import vbn.state.value.ISymbol;

import java.io.Serializable;

public interface IConstraint extends Serializable {

    @NonNull Boolean isBranch();

    void setIsBranch(boolean isBranch);

    /**
     * Get the "line number" where the constraint was in Jimple
     * (Implemented manually in instrumentation using a counter)
     * @return the "line number"
     */
    long getUniqueId();

    /**
     * Currently prints
     */
    @Override
    String toString();

    /**
     * Get the value of the expression, whether it is true or false
     * @return whether constraint evaluated to true
     */
    boolean getOriginalEvaluation();

    /**
     * Set the symbol this constraint is being assigned to
     */
    void setAssignmentSymbol(ISymbol symbol);

    void setConstraintNumber(int constraintNumber);
}
