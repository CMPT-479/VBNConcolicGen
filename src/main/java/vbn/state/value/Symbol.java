package vbn.state.value;

import java.io.Serializable;
import java.util.Objects;

public class Symbol extends AbstractSymbol implements Serializable {

    // For Serializable
    private static final long serialVersionUID = 0L;

    /**
     * The unique identifier for this string
     */
    public String varName;

    /**
     * The current concrete value for this symbol
     */
    public Object value;

    /**
     * Whether this variable represents an original input variables
     */
    public boolean isInput = false;

    public Symbol(String varName, Type symbolType, Object value) {
        this.varName = varName;
        this.type = symbolType;
        this.value = value;
    }
    public Symbol(String varName, Type symbolType, Object value, boolean isInput) {
        this.varName = varName;
        this.type = symbolType;
        this.value = value;
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
        return Objects.equals(this.varName, otherSymbol.varName) && this.type == otherSymbol.type;
    }
}
