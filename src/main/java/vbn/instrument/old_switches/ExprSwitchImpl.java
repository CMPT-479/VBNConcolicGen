package vbn.instrument.old_switches;

import vbn.instrument.InstrumentData;
import soot.Unit;
import soot.jimple.AbstractExprSwitch;

public class ExprSwitchImpl extends AbstractExprSwitch<Boolean> {
    public InstrumentData data;
    public Unit unit;
    public ExprSwitchImpl(InstrumentData data, Unit unit) {
        setResult(false);
        this.data = data;
        this.unit = unit;
    }
}
