package vbn.state.value;

import java.io.Serializable;

public class SymbolIntConstant extends AbstractSymbolConstant implements Serializable {
    public int value;
    public SymbolIntConstant(String id, int value) {
        this.value = value;
        this.type = Type.INT_TYPE;
    }
    @Override
    public Serializable getValue() {
        return value;
    }
}
