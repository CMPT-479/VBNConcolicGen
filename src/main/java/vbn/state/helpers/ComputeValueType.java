package vbn.state.helpers;

import vbn.state.value.Value;

public class ComputeValueType {
    public static Value.Type getType(Object concreteValue) {
        return Value.Type.UNKNOWN;
    }
}