package vbn.constraints.Value;

public abstract class Value {
    public enum Type {
        INT_TYPE,
        REAL_TYPE,
        BOOL_TYPE
    }

    public Type type;

    public abstract Object getValue();

}


