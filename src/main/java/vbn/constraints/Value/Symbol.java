package vbn.constraints.Value;

import java.io.Serializable;
import java.util.Objects;

public class Symbol extends AbstractSymbol implements Serializable {

    // For Serializable
    private static final long serialVersionUID = 0L;

    /**
     * The unique identifier for this string
     */
    public String id;

    /**
     * The current concrete value for this symbol
     */
    public Object value;

    /**
     * Whether this variable represents an original input variables
     */
    public boolean isInput = false;

    public Symbol(String id, Type symbolType, Object value) {
        this.id = id;
        this.type = symbolType;
        this.value = null;
    }
    public Symbol(String id, Type symbolType, Object value, boolean isInput) {
        this.id = id;
        this.type = symbolType;
        this.value = null;
        this.isInput = isInput;
    }

    @Override
    public Object getValue() {
        return value;
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
