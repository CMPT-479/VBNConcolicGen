package vbn.constraints.Value;

import java.io.Serializable;

public class Symbol extends AbstractSymbolConstant implements Serializable {

    private static final long serialVersionUID = 0L;

    public String id;

    public Symbol(String id, ValueType symbolType) {
        this.id = id;
        this.valueType = symbolType;
    }
}
