package vbn.instrument.switches;

import soot.*;
import soot.jimple.Jimple;
import vbn.instrument.InstrumentData;

import java.util.List;

public class ValueTypeSwitch extends TypeSwitch<Boolean> {
    public InstrumentData data;
    public Value v;
    public Unit unit;
    public boolean isPushed;
    private final SootMethod method;

    public ValueTypeSwitch(InstrumentData data, Unit unit, Value v, boolean isPushed) {
        this.data = data;
        this.v = v;
        this.unit = unit;
        this.isPushed = isPushed;
        String methodName = isPushed ? "pushValue" : "loadValue";
        method = data.runtime.getMethod(
                methodName,
                List.of(RefType.v("java.lang.Object"))
        );
    }

    @Override
    public void caseIntType(IntType t) {
        Value box = makeBoxedValue(v, "int", "java.lang.Integer");
        instrument(box);
    }

    public Value makeBoxedValue(Value v, String type, String boxedType) {
        var boxMethod = Scene.v().getMethod(String.format("<%s: %s valueOf(%s)>", boxedType, boxedType, type));
        var boxLocal = Jimple.v().newLocal(String.format("_b__%d", data.body.getLocalCount()), RefType.v(boxedType));
        data.body.getLocals().add(boxLocal);
        var expr = Jimple.v().newStaticInvokeExpr(boxMethod.makeRef(), v);
        var assignment = Jimple.v().newAssignStmt(boxLocal, expr);
        if (isPushed)
            data.units.insertBefore(assignment, unit);
        else
            data.units.insertAfter(assignment, unit);
        return boxLocal;
    }

    private void instrument(Value v) {
        var caller = Jimple.v().newStaticInvokeExpr(method.makeRef(), v);
        var invokeStmt = Jimple.v().newInvokeStmt(caller);
        if (isPushed)
            data.units.insertBefore(invokeStmt, unit);
        else
            data.units.insertAfter(invokeStmt, unit);
        setResult(true);
    }
}
