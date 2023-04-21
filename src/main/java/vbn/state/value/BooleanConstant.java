package vbn.state.value;

public class BooleanConstant implements IConstant {

    // For Serializable
    private static final long serialVersionUID = 0L;

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
