package vbn.instrument.switches;

import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;
import vbn.instrument.InstrumentData;

import java.util.List;

public abstract class AbstractReferenceSwitch extends AbstractSwitch {
    public AbstractReferenceSwitch(InstrumentData data) {
        super(data);
    }

    public void caseLocal(Local v) {
        instrument(v.getName(), v);
    }

    public void caseArrayRef(ArrayRef v) {
        instrument(v.toString(), v);
    }

    public void caseInstanceFieldRef(InstanceFieldRef v) {
        var base = v.getBase();
        var field = v.getField().getName();
        instrument(String.format("%s.%s", base, field), v);
    }

    @Override
    public void caseStaticFieldRef(StaticFieldRef v) {
        var className = v.getField().getDeclaringClass().getName();
        var field = v.getField().getName();
        instrument(String.format("%s.%s", className, field), v);
    }
    abstract void instrument(String symbol, Value value);
}
