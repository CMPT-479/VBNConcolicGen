package vbn.state.value;

import java.io.Serializable;
import java.util.Objects;

public class BooleanSymbol implements AbstractSymbol, Serializable {

    // For Serializable
    private static final long serialVersionUID = 0L;

    /**
     * The unique identifier for this string
     */
    public String varName;

    /**
     * The current concrete value for this symbol
     */
    public boolean value;

    public BooleanSymbol(String varName, boolean value) {
        this.varName = varName;
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.BOOL_TYPE;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BooleanSymbol)) {
            return false;
        }
        BooleanSymbol otherSymbol = (BooleanSymbol) obj;
        return Objects.equals(this.varName, otherSymbol.varName) && this.getType() == otherSymbol.getType();
    }
}
