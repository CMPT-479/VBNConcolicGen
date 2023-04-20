package vbn.state.value;

import java.io.Serializable;

public abstract class Value implements Serializable {
    public enum Type implements Serializable {
        INT_TYPE,
        REAL_TYPE,
        BOOL_TYPE
    }

    public Type type;

    public abstract Serializable getValue();

}


