package vbn.instrument.switches;

import soot.*;
import soot.jimple.*;
import vbn.instrument.InstrumentData;

import java.util.List;

public class ReferenceSwitch extends AbstractJimpleValueSwitch<Object> {
    public final InstrumentData data;
    public final Unit unit;
    public static SootMethod methodWithInt, methodWithObject;
    public final boolean insertBefore;

    public ReferenceSwitch(InstrumentData data, Unit unit, String methodName, boolean insertBefore) {
        this.data = data;
        this.unit = unit;
        methodWithInt = data.runtime.getMethod(methodName, List.of(
                IntType.v(),
                IntType.v()
        ));
        methodWithObject = data.runtime.getMethod(methodName, List.of(
                RefType.v("java.lang.Object"),
                IntType.v()
        ));
        this.insertBefore = insertBefore;
    }

    public void caseLocal(Local v) {
        var base = IntConstant.v(0);
        var id = IntConstant.v(data.symbolTable.getId(v.getName()));
        insertCall(methodWithInt, base, id);
    }

    public void caseArrayRef(ArrayRef v) {
        var base = v.getBase(); // Object
        var index = v.getIndex(); // Int
        insertCall(methodWithObject, base, index);
    }

    public void caseInstanceFieldRef(InstanceFieldRef v) {
        var base = v.getBase();
        var fieldId = data.symbolTable.getId(v.getField().getName());
        insertCall(methodWithObject, base, IntConstant.v(fieldId));
    }

    @Override
    public void caseStaticFieldRef(StaticFieldRef v) {
        var className = v.getField().getDeclaringClass().getName();
        var field = v.getField().getName();
        insertCall(methodWithInt,
                IntConstant.v(data.symbolTable.getId(className)),
                IntConstant.v(data.symbolTable.getId(field)));
    }

    private void insertCall(SootMethod method, Value v1, Value v2) {
        var caller = Jimple.v().newStaticInvokeExpr(method.makeRef(), v1, v2);
        var stmt = Jimple.v().newInvokeStmt(caller);
        if (insertBefore) {
            data.units.insertBefore(stmt, unit);
        } else {
            data.units.insertAfter(stmt, unit);
        }
    }
}
