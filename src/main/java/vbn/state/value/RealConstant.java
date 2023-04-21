package vbn.state.value;

public class RealConstant implements IConstant {
    public double value;

    public RealConstant(double value) {
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.REAL_TYPE;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
