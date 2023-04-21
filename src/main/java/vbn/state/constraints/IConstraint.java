package vbn.state.constraints;

import java.io.Serializable;

public interface IConstraint extends Serializable {

    int getLineNumber();

    void setLineNumber(int lineNumber);
    void print();
}
