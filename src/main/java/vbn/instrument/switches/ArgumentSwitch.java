package vbn.instrument.switches;

import soot.*;
import soot.jimple.*;
import vbn.instrument.InstrumentData;

import java.util.List;

public class ArgumentSwitch extends AbstractJimpleValueSwitch<Object> {
    InstrumentData data;
    Unit unit;
    SootMethod method;
    SootMethod methodForConstant;

    public ArgumentSwitch(InstrumentData data, Unit unit) {
        this.data = data;
        this.unit = unit;
        method = data.runtime.getMethod("pushArg", List.of(
                RefType.v("java.lang.String"),
                RefType.v("java.lang.Object")
        ));
        methodForConstant = data.runtime.getMethod("pushArgConst", List.of(
                RefType.v("java.lang.Object")
        ));
    }

    public void caseLocal(Local v) {
        insertCall(v.getName(), v);
    }

    public void caseArrayRef(ArrayRef v) {
        insertCall(v.toString(), v);
    }

    public void caseInstanceFieldRef(InstanceFieldRef v) {
        var base = v.getBase();
        var field = v.getField().getName();
        insertCall(String.format("%s.%s", base, field), v);
    }

    private void insertCall(String id, Value v) {
        var typeSwitch = new ValueTypeSwitch(data, unit, v);
        v.getType().apply(typeSwitch);
        var units = typeSwitch.getResult();
        var caller = Jimple.v().newStaticInvokeExpr(method.makeRef(), StringConstant.v(id), typeSwitch.v);
        units.add(Jimple.v().newInvokeStmt(caller));
        data.units.insertBefore(units, unit);
    }

    @Override
    public void defaultCase(Object obj) {
        if (!(obj instanceof Constant)) return;
        var v = (Constant) obj;
        var typeSwitch = new ValueTypeSwitch(data, unit, v);
        v.getType().apply(typeSwitch);
        var units = typeSwitch.getResult();
        var caller = Jimple.v().newStaticInvokeExpr(methodForConstant.makeRef(), typeSwitch.v);
        units.add(Jimple.v().newInvokeStmt(caller));
        data.units.insertBefore(units, unit);
    }
}
