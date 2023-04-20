package vbn.state.value;

public class SymbolRealConstant extends AbstractSymbolConstant {
    public int value;
    public SymbolRealConstant(String id, int value) {
        this.value = value;
        this.valueType = ValueType.REAL_TYPE;
    }
}
