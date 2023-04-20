package vbn.state.value;

import java.io.Serializable;

public class SymbolBooleanConstant extends AbstractSymbolConstant implements Serializable {
    public boolean value;
    public SymbolBooleanConstant(String id, boolean value) {
        this.value = value;
        this.type = Type.BOOL_TYPE;
    }
    @Override
    public Serializable getValue() {
        return value;
    }
}
