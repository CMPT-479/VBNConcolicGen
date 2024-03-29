package vbn.state.value;

import java.util.Objects;

public class IntSymbol implements ISymbol {

    // For Serializable
    private static final long serialVersionUID = 0L;

    /**
     * The unique identifier for this string
     */
    public String varName;

    /**
     * The current concrete value for this symbol
     */
    private long concreteValue;

    public IntSymbol(String varName, long concreteValue) {
        this.varName = varName;
        this.concreteValue = concreteValue;
    }

    @Override
    public String getName() {
        return varName;
    }

    @Override
    public Type getType() {
        return Type.INT_TYPE;
    }

    @Override
    public Object getValue() {
        return concreteValue;
    }

    @Override
    public void setValue(Object value) {
        this.concreteValue = (int) value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntSymbol)) {
            return false;
        }
        IntSymbol otherSymbol = (IntSymbol) obj;
        return Objects.equals(this.varName, otherSymbol.varName) && this.getType() == otherSymbol.getType();
    }

    @Override
    public String toString() {
        var result = "IntSymbol {";
        result += "name: " + getName();
        result += ", con_val: " + getValue().toString();
        result += "}";
        return result;
    }
}
