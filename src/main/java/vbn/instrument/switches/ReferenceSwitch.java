package vbn.instrument.switches;

import soot.*;
import soot.jimple.*;
import vbn.instrument.InstrumentData;

import java.util.List;

public class ReferenceSwitch extends AbstractJimpleValueSwitch<Object> {
    public final InstrumentData data;
    public final Unit unit;
    public static SootMethod methodWithInt, methodWithObject;
    public boolean insertBefore = false;

    public ReferenceSwitch(InstrumentData data, Unit unit, String methodName) {
        this.data = data;
        this.unit = unit;
        methodWithInt = data.runtime.getMethod(methodName, List.of(
                IntType.v(),
                IntType.v(),
                RefType.v("java.lang.Object")
        ));
        methodWithObject = data.runtime.getMethod(methodName, List.of(
                RefType.v("java.lang.Object"),
                IntType.v(),
                RefType.v("java.lang.Object")
        ));
    }

    public ReferenceSwitch setInsertBefore(boolean insertBefore) {
        this.insertBefore = insertBefore;
        return this;
    }

    public void caseLocal(Local v) {
        var base = IntConstant.v(0);
        var id = IntConstant.v(data.symbolTable.getId(v.getName()));
        insertCall(methodWithInt, base, id, v);
    }

    public void caseArrayRef(ArrayRef v) {
        var base = v.getBase(); // Object
        var index = v.getIndex(); // Int
        insertCall(methodWithObject, base, index, v);
    }

    public void caseInstanceFieldRef(InstanceFieldRef v) {
        var base = v.getBase();
        var fieldId = data.symbolTable.getId(v.getField().getName());
        insertCall(methodWithObject, base, IntConstant.v(fieldId), v);
    }

    @Override
    public void caseStaticFieldRef(StaticFieldRef v) {
        var className = v.getField().getDeclaringClass().getName();
        var field = v.getField().getName();
        insertCall(methodWithInt,
                IntConstant.v(data.symbolTable.getId(className)),
                IntConstant.v(data.symbolTable.getId(field)), v);
    }

    private void insertCall(SootMethod method, Value v1, Value v2, Value v3) {
        var caller = Jimple.v().newStaticInvokeExpr(method.makeRef(), v1, v2, v3);
        var stmt = Jimple.v().newInvokeStmt(caller);
        if (insertBefore) {
            data.units.insertBefore(stmt, unit);
        } else {
            data.units.insertAfter(stmt, unit);
        }
    }
}
