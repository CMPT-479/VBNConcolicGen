package vbn.constraints;

import java.io.Serializable;

public class Symbol implements Serializable {

    private static final long serialVersionUID = 0L;

    public String id;
    public enum SymbolType {
        INT_TYPE,
        REAL_TYPE,
        BOOL_TYPE
    }
    public SymbolType symbolType;

    Symbol(String id, SymbolType symbolType) {
        this.id = id;
        this.symbolType = symbolType;
    }
}
