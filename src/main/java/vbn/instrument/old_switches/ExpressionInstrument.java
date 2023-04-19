package vbn.instrument.old_switches;

import vbn.instrument.InstrumentData;
import soot.Unit;
import soot.Value;

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
        v.apply(new CallAddressSwitch(data, unit, new CallAddressSwitch.Setting(false, "loadAddress")));
    }
}
