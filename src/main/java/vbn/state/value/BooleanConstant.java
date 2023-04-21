package vbn.state.value;

public class BooleanConstant implements IConstant {
    public boolean value;
    public BooleanConstant(boolean value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.BOOL_TYPE;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
