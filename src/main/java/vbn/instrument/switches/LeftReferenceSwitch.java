package vbn.instrument.switches;

import soot.*;
import soot.jimple.*;
import vbn.instrument.InstrumentData;

import java.util.List;

public class LeftReferenceSwitch extends AbstractJimpleValueSwitch<Object> {
    public final InstrumentData data;
    public final Unit unit;
    public static SootMethod methodWithInt, methodWithObject;
    public boolean insertBefore = false;

    public LeftReferenceSwitch(InstrumentData data, Unit unit) {
        this.data = data;
        this.unit = unit;
        methodWithInt = data.runtime.getMethod("finalizeStore", List.of(
                IntType.v(),
                IntType.v()
        ));
        methodWithObject = data.runtime.getMethod("finalizeStore", List.of(
                RefType.v("java.lang.Object"),
                IntType.v()
        ));
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

    private void insertCall(SootMethod method, Value base, Value field) {
        var caller = Jimple.v().newStaticInvokeExpr(method.makeRef(), base, field);
        Unit stmt = Jimple.v().newInvokeStmt(caller);
        data.units.insertAfter(stmt, unit);
    }
}
