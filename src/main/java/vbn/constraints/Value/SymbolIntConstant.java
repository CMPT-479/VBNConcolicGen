package vbn.constraints.Value;

public class SymbolIntConstant extends AbstractSymbolConstant {
    public int value;
    public SymbolIntConstant(String id, int value) {
        this.value = value;
        this.valueType = ValueType.INT_TYPE;
    }
}
