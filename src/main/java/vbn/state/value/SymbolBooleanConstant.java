package vbn.state.value;

public class SymbolBooleanConstant extends AbstractSymbolConstant {
    public boolean value;
    public SymbolBooleanConstant(String id, boolean value) {
        this.value = value;
        this.valueType = ValueType.BOOL_TYPE;
    }
}
