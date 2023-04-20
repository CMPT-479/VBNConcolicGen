package vbn.state.value;

public class UnknownConstant implements AbstractConstant {
    public Object value;
    public UnknownConstant(Object value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.UNKNOWN;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
