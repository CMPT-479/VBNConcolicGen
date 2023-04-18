package org.cmpt479.instrument.old_switches;

import org.cmpt479.instrument.InstrumentData;
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
