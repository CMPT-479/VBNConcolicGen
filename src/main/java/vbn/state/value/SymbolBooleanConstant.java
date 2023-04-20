package vbn.state.value;

public class SymbolBooleanConstant extends AbstractSymbolConstant {
    public boolean value;
    public SymbolBooleanConstant(String id, boolean value) {
        this.value = value;
        this.type = Type.BOOL_TYPE;
    }
    @Override
    public Object getValue() {
        return value;
    }
}
