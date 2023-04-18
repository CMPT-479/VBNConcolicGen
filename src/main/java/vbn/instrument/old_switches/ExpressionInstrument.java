package vbn.instrument.old_switches;

import vbn.instrument.InstrumentData;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.Constant;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;

public class ExpressionInstrument {
    public static class Args {
        Value left;
        boolean conditional;

        public Args(Value left, boolean conditional) {
            this.left = left;
            this.conditional = conditional;
        }
    }
    public static void instrument(Value v, Unit unit, InstrumentData data, Args args) {
        v.apply(new CallAddressSwitch(data, unit, new CallAddressSwitch.Setting(false, false, "loadAddress")));
        if (v instanceof Local || v instanceof ArrayRef
                || v instanceof InstanceFieldRef || v instanceof StaticFieldRef
                || v instanceof Constant) {
            v.getType().apply(new ValueSwitch(data, v, unit, new ValueSwitch.Setting(false, false, "loadValue")));
        }
    }
}
