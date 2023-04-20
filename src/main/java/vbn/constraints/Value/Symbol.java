package vbn.constraints.Value;

import java.io.Serializable;
import java.util.Objects;

public class Symbol extends AbstractSymbolConstant implements Serializable {

    private static final long serialVersionUID = 0L;

    public String id;

    public Symbol(String id, ValueType symbolType) {
        this.id = id;
        this.valueType = symbolType;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Symbol)) {
            return false;
        }
        Symbol otherSymbol = (Symbol) obj;
        return Objects.equals(this.id, otherSymbol.id) && this.valueType == otherSymbol.valueType;
    }
}
