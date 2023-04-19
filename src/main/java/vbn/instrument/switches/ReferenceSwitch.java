package vbn.instrument.switches;

import soot.*;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.Jimple;
import soot.jimple.StringConstant;
import vbn.instrument.InstrumentData;

import java.util.List;

public class ReferenceSwitch extends AbstractJimpleValueSwitch<Object> {
    public final InstrumentData data;
    public final Unit unit;
    private final SootMethod method;
    public final boolean insertBefore;

    public ReferenceSwitch(InstrumentData data, Unit unit, String methodName, boolean insertBefore) {
        this.data = data;
        this.unit = unit;
        List<Type> args = List.of(RefType.v("java.lang.String"));
        method = data.runtime.getMethod(methodName, args);
        this.insertBefore = insertBefore;
    }

    public void caseLocal(Local v) {
        var id = data.symbolTable.getIdentifier(v.getName());
        var name = StringConstant.v(id);
        var caller = Jimple.v().newStaticInvokeExpr(method.makeRef(), name);
        var stmt = Jimple.v().newInvokeStmt(caller);
        if (insertBefore) {
            data.units.insertBefore(stmt, unit);
        } else {
            data.units.insertAfter(stmt, unit);
        }
    }
}
