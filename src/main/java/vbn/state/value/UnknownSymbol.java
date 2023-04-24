package vbn.state.value;

import java.io.Serializable;
import java.util.Objects;

public class UnknownSymbol implements ISymbol, IUnknownValue, Serializable {

    // For Serializable
    private static final long serialVersionUID = 0L;

    /**
     * The unique identifier for this string
     */
    private String varName;

    /**
     * The current concrete value for this symbol
     */
    private Object value;

    public UnknownSymbol(String varName, Object value) {
        this.varName = varName;
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.UNKNOWN;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getName() {
        return varName;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UnknownSymbol)) {
            return false;
        }
        UnknownSymbol otherSymbol = (UnknownSymbol) obj;
        return Objects.equals(this.varName, otherSymbol.varName) && this.getType() == otherSymbol.getType();
    }

    @Override
    public String toString() {
        var result = "UnknownSym {";
        result += "name: " + getName();
        result += ", con_val: " + getValue().toString();
        result += "}";
        return result;
    }
}
