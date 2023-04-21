package vbn.instrument.switches;

import soot.*;
import soot.jimple.*;
import vbn.instrument.InstrumentData;

import java.util.ArrayList;
import java.util.List;

public class LeftReferenceSwitch extends AbstractReferenceSwitch {
    public SootMethod method;
    public LeftReferenceSwitch(InstrumentData data) {
        super(data);
        method = data.runtime.getMethod("finalizeStore", List.of(
                RefType.v("java.lang.String"),
                RefType.v("java.lang.Object")
        ));
    }

    void instrument(String id, Value v) {
        var typeSwitch = new ValueTypeSwitch(data, v);
        v.getType().apply(typeSwitch);
        getResult().afterUnits.addAll(typeSwitch.getResult());
        var caller = Jimple.v().newStaticInvokeExpr(method.makeRef(), StringConstant.v(id), typeSwitch.v);
        getResult().afterUnits.add(Jimple.v().newInvokeStmt(caller));
    }
}
