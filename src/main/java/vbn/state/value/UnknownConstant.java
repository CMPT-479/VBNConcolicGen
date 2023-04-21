package vbn.state.value;

public class UnknownConstant implements IConstant, IUnknownValue {

    // For Serializable
    private static final long serialVersionUID = 0L;
    
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
