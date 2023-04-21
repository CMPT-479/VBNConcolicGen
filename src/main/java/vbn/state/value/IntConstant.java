package vbn.state.value;

public class IntConstant implements IConstant {

    // For Serializable
    private static final long serialVersionUID = 0L;

    private final int value;


    public IntConstant(int value) {
        this.value = value;
    }
    @Override
    public Object getValue() {
        return value;
    }
    @Override
    public Type getType() {
        return Type.INT_TYPE;
    }

    @Override
    public String toString() {
        return "IntConstant {value: " + getValue().toString() + "}";
    }
}
