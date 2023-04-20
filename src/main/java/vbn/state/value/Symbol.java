package vbn.state.value;

import soot.Type;

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
    public Serializable value;

    /**
     * Whether this variable represents an original input variables
     */
    public boolean isInput = false;

    public Symbol(String id, Type symbolType, Serializable value) {
        this.id = id;
        this.type = symbolType;
        this.value = value;
    }
    public Symbol(String id, Type symbolType, Serializable value, boolean isInput) {
        this.id = id;
        this.type = symbolType;
        this.value = value;
        this.isInput = isInput;
    }

    @Override
    public Serializable getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Symbol)) {
            return false;
        }
        Symbol otherSymbol = (Symbol) obj;
        return Objects.equals(this.id, otherSymbol.id) && this.type == otherSymbol.type;
    }
}
