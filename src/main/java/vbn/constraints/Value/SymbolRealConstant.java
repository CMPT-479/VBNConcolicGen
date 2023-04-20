package vbn.constraints.Value;

public class SymbolRealConstant extends AbstractSymbolConstant {
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
