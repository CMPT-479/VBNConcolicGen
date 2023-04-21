package vbn.instrument.switches;

import soot.*;
import soot.jimple.*;
import vbn.instrument.InstrumentData;

import java.util.ArrayList;
import java.util.List;

public class RightReferenceSwitch extends AbstractReferenceSwitch {
    public static SootMethod method, methodWithConstant;

    public RightReferenceSwitch(InstrumentData data) {
        super(data);
        method = data.runtime.getMethod("pushSym", List.of(
                RefType.v("java.lang.String"),
                RefType.v("java.lang.Object")
        ));
        methodWithConstant = data.runtime.getMethod("pushConstant", List.of(
                RefType.v("java.lang.Object")
        ));
    }

    void instrument(String id, Value value) {
        var typeSwitch = new ValueTypeSwitch(data, value);
        value.getType().apply(typeSwitch);
        getResult().beforeUnits.addAll(typeSwitch.getResult());
        var caller = Jimple.v().newStaticInvokeExpr(method.makeRef(), StringConstant.v(id), typeSwitch.v);
        getResult().beforeUnits.add(Jimple.v().newInvokeStmt(caller));
    }
}
