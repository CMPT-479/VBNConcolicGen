package vbn.state.value;

import java.io.Serializable;

public interface AbstractSymbol extends Value, Serializable {
    String getName();

    void setValue(Object value);
}
