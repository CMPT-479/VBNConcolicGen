package vbn.constraints;

import java.io.Serializable;

public class Symbol implements Serializable {
    public String id;
    public enum SymbolType {
        INT_TYPE,
        BOOL_TYPE
    }
    public SymbolType symbolType;

    Symbol(String id, SymbolType symbolType) {
        this.id = id;
        this.symbolType = symbolType;
    }
}
