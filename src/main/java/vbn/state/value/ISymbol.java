package vbn.state.value;

import java.io.Serializable;

public interface ISymbol extends Value, Serializable {
    String getName();

    void setValue(Object value);
}
