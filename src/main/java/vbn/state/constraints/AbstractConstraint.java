package vbn.state.constraints;

import java.io.Serializable;

public interface AbstractConstraint extends Serializable {

    int getLineNumber();
    void setLineNumber(int lineNumber);
}
