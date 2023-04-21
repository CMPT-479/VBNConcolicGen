package vbn.state.value;

import java.io.Serializable;
import java.util.Objects;

public class BooleanSymbol implements ISymbol {

    // For Serializable
    private static final long serialVersionUID = 0L;

    /**
     * The unique identifier for this string
     */
    private String varName;

    /**
     * The current concrete value for this symbol
     */
    private boolean concreteValue;

    public BooleanSymbol(String varName, boolean concreteValue) {
        this.varName = varName;
        this.concreteValue = concreteValue;
    }

    @Override
    public Value.Type getType() {
        return Value.Type.BOOL_TYPE;
    }

    @Override
    public Object getValue() {
        return concreteValue;
    }

    @Override
    public void setValue(Object value) {
        this.concreteValue = (boolean) value;
    }

    @Override
    public String getName() {
        return varName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BooleanSymbol)) {
            return false;
        }
        BooleanSymbol otherSymbol = (BooleanSymbol) obj;
        return Objects.equals(this.varName, otherSymbol.varName) && this.getType() == otherSymbol.getType();
    }

    @Override
    public String toString() {
        return "BooleanSymbol {name: " + getName() + ", concrete_value: " + getValue().toString() + "}";
    }
}
