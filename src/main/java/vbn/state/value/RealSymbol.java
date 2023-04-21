package vbn.state.value;

import java.io.Serializable;
import java.util.Objects;

public class RealSymbol implements ISymbol, Serializable {

    // For Serializable
    private static final long serialVersionUID = 0L;

    /**
     * The unique identifier for this string
     */
    private String varName;

    /**
     * The current concrete value for this symbol
     */
    private double concreteValue;

    public RealSymbol(String varName, double concreteValue) {
        this.varName = varName;
        this.concreteValue = concreteValue;
    }

    @Override
    public Type getType() {
        return Type.REAL_TYPE;
    }

    @Override
    public Object getValue() {
        return concreteValue;
    }

    @Override
    public void setValue(Object value) {
        this.concreteValue = (double) value;
    }

    @Override
    public String getName() {
        return varName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RealSymbol)) {
            return false;
        }
        RealSymbol otherSymbol = (RealSymbol) obj;
        return Objects.equals(this.varName, otherSymbol.varName) && this.getType() == otherSymbol.getType();
    }
}
