package vbn.state.value;

public class SymbolIntConstant extends AbstractSymbolConstant {
    public int value;
    public SymbolIntConstant(String id, int value) {
        this.value = value;
        this.type = Type.INT_TYPE;
    }
    @Override
    public Object getValue() {
        return value;
    }
}
