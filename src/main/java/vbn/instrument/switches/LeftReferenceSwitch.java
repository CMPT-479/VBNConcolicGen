package vbn.instrument.switches;

import soot.*;
import soot.jimple.*;
import vbn.instrument.InstrumentData;

import java.util.List;

public class LeftReferenceSwitch extends AbstractJimpleValueSwitch<Object> {
    public final InstrumentData data;
    public final Unit unit;
    public static SootMethod method;
    public LeftReferenceSwitch(InstrumentData data, Unit unit) {
        this.data = data;
        this.unit = unit;
        method = data.runtime.getMethod("finalizeStore", List.of(
                RefType.v("java.lang.String")
        ));
    }

    public void caseLocal(Local v) {
        insertCall(method, v.getName());
    }

    public void caseArrayRef(ArrayRef v) {
         insertCall(method, v.toString());
    }

    public void caseInstanceFieldRef(InstanceFieldRef v) {
        var base = v.getBase();
        var field = v.getField().getName();
        insertCall(method, String.format("%s.%s", base, field));
    }

    @Override
    public void caseStaticFieldRef(StaticFieldRef v) {
        var className = v.getField().getDeclaringClass().getName();
        var field = v.getField().getName();
        insertCall(method, String.format("%s.%s", className, field));
    }

    private void insertCall(SootMethod method, String id) {
        var caller = Jimple.v().newStaticInvokeExpr(method.makeRef(), StringConstant.v(id));
        Unit stmt = Jimple.v().newInvokeStmt(caller);
        data.units.insertAfter(stmt, unit);
    }
}
