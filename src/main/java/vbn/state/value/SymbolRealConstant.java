package vbn.state.value;

public class SymbolRealConstant extends AbstractConstant {
    public int value;
    public SymbolRealConstant(String id, int value) {
        this.value = value;
        this.type = Type.REAL_TYPE;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
