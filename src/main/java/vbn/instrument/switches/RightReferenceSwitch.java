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

    @Override
    public void defaultCase(Object obj) {
        if (obj instanceof Constant) {
            var v = (Constant) obj;
            var typeSwitch = new ValueTypeSwitch(data, v);
            v.getType().apply(typeSwitch);
            getResult().beforeUnits.addAll(typeSwitch.getResult());
            var caller = Jimple.v().newStaticInvokeExpr(methodWithConstant.makeRef(), typeSwitch.v);
            getResult().beforeUnits.add(Jimple.v().newInvokeStmt(caller));
        }
    }

    void instrument(String id, Value value) {
        var typeSwitch = new ValueTypeSwitch(data, value);
        value.getType().apply(typeSwitch);
        getResult().beforeUnits.addAll(typeSwitch.getResult());
        var caller = Jimple.v().newStaticInvokeExpr(method.makeRef(), StringConstant.v(id), typeSwitch.v);
        getResult().beforeUnits.add(Jimple.v().newInvokeStmt(caller));
    }
}
