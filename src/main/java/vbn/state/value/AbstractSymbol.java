package vbn.state.value;

import vbn.state.value.Value;

import java.io.Serializable;

public abstract class AbstractSymbol extends Value implements Serializable {
    public String id;
}
