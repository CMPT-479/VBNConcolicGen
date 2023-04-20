package vbn.state.value;

public class IntConstant implements AbstractConstant {
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
}
