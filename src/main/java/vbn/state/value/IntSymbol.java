package vbn.state.value;

import java.io.Serializable;
import java.util.Objects;

public class IntSymbol implements AbstractSymbol {

    // For Serializable
    private static final long serialVersionUID = 0L;

    /**
     * The unique identifier for this string
     */
    public String varName;

    /**
     * The current concrete value for this symbol
     */
    public int value;

    public IntSymbol(String varName, int value) {
        this.varName = varName;
        this.value = value;
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
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (int) value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntSymbol)) {
            return false;
        }
        IntSymbol otherSymbol = (IntSymbol) obj;
        return Objects.equals(this.varName, otherSymbol.varName) && this.getType() == otherSymbol.getType();
    }
}
