package vbn.instrument.switches;

import soot.*;
import soot.jimple.*;
import vbn.instrument.InstrumentData;

import java.util.List;

public class RightReferenceSwitch extends AbstractJimpleValueSwitch<Object> {
    public final InstrumentData data;
    public final Unit unit;
    public static SootMethod method;

    public RightReferenceSwitch(InstrumentData data, Unit unit) {
        this.data = data;
        this.unit = unit;
        method = data.runtime.getMethod("pushSym", List.of(
                RefType.v("java.lang.String"),
                RefType.v("java.lang.Object")
        ));
    }

    public void caseLocal(Local v) {
        insertCall(method, v.getName(), v);
    }

    public void caseArrayRef(ArrayRef v) {
        insertCall(method, v.toString(), v);
    }

    public void caseInstanceFieldRef(InstanceFieldRef v) {
        insertCall(method, String.format("%s.%s", v.getBase(), v.getField().getName()), v);
    }

    @Override
    public void caseStaticFieldRef(StaticFieldRef v) {
        var className = v.getField().getDeclaringClass().getName();
        var field = v.getField().getName();
        insertCall(method, String.format("%s.%s", className, field), v);
    }

    private void insertCall(SootMethod method, String id, Value value) {
        var typeSwitch = new ValueTypeSwitch(data, unit, value);
        value.getType().apply(typeSwitch);
        var caller = Jimple.v().newStaticInvokeExpr(method.makeRef(), StringConstant.v(id), typeSwitch.v);
        Unit stmt = Jimple.v().newInvokeStmt(caller);
        data.units.insertBefore(stmt, unit);
    }
}
