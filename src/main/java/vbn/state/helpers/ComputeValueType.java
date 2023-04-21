package vbn.state.helpers;

import vbn.state.value.Value;

public class ComputeValueType {
    public static Value.Type getType(Object concreteValue) {
        if (concreteValue instanceof Byte) {
            return Value.Type.INT_TYPE;
        }
        else if (concreteValue instanceof Character) {
            return Value.Type.INT_TYPE;
        }
        else if (concreteValue instanceof Short) {
            return Value.Type.INT_TYPE;
        }
        else if (concreteValue instanceof Integer) {
            return Value.Type.INT_TYPE;
        }
        else if (concreteValue instanceof Long) {
            return Value.Type.REAL_TYPE;
        }
        else if (concreteValue instanceof Float) {
            return Value.Type.REAL_TYPE;
        }
        else if (concreteValue instanceof Double) {
            return Value.Type.REAL_TYPE;
        }
        else if (concreteValue instanceof Boolean) {
            return Value.Type.REAL_TYPE;
        }
        return Value.Type.UNKNOWN;
    }
}