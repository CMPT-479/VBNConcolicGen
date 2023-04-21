package vbn.state.constraints;

import java.io.Serializable;

public interface IConstraint extends Serializable {
    boolean hasLineNumber();
    int getLineNumber();
    void setLineNumber(int lineNumber);
    void print();
    boolean getOriginalEvaluation();
}
