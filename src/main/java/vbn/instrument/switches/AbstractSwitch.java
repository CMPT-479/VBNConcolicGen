package vbn.instrument.switches;

import soot.Unit;
import soot.jimple.AbstractJimpleValueSwitch;
import vbn.instrument.InstrumentData;
import vbn.instrument.InstrumentResult;

import java.util.List;

public abstract class AbstractSwitch extends AbstractJimpleValueSwitch<InstrumentResult> {
    public final InstrumentData data;

    public AbstractSwitch(InstrumentData data) {
        this.data = data;
        setResult(new InstrumentResult());
    }
}
