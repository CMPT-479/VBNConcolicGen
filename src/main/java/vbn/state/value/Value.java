package vbn.state.value;

public interface Value {
    public enum Type {
        INT_TYPE,
        REAL_TYPE,
        BOOL_TYPE,
        UNKNOWN,
    }

    Type getType();

    Object getValue();

}


