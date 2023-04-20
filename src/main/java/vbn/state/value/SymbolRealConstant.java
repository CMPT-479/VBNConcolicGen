package vbn.state.value;

import java.io.Serializable;

public class SymbolRealConstant extends AbstractSymbolConstant implements Serializable {
    public int value;
    public SymbolRealConstant(String id, int value) {
        this.value = value;
        this.type = Type.REAL_TYPE;
    }

    @Override
    public Serializable getValue() {
        return value;
    }
}
