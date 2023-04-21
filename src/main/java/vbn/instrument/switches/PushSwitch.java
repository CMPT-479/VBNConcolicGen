package vbn.instrument.switches;

import soot.*;
import soot.jimple.*;
import vbn.instrument.InstrumentData;

import java.util.List;

public class PushSwitch extends AbstractReferenceSwitch {
    SootMethod method;
    SootMethod methodWithConstant;

    public PushSwitch(InstrumentData data) {
        super(data);
        method = data.runtime.getMethod("pushArg", List.of(
                RefType.v("java.lang.String"),
                RefType.v("java.lang.Object")
        ));
        methodWithConstant = data.runtime.getMethod("pushArgConst", List.of(
                RefType.v("java.lang.Object")
        ));
    }

    void instrument(String id, Value v) {
        var typeSwitch = new ValueTypeSwitch(data, v);
        v.getType().apply(typeSwitch);
        getResult().beforeUnits.addAll(typeSwitch.getResult());
        var caller = Jimple.v().newStaticInvokeExpr(method.makeRef(), StringConstant.v(id), typeSwitch.v);
        getResult().beforeUnits.add(Jimple.v().newInvokeStmt(caller));
    }

    @Override
    public void defaultCase(Object obj) {
        if (!(obj instanceof Constant)) return;
        var v = (Constant) obj;
        var typeSwitch = new ValueTypeSwitch(data, v);
        v.getType().apply(typeSwitch);
        getResult().beforeUnits.addAll(typeSwitch.getResult());
        var caller = Jimple.v().newStaticInvokeExpr(methodWithConstant.makeRef(), typeSwitch.v);
        getResult().beforeUnits.add(Jimple.v().newInvokeStmt(caller));
    }
}
