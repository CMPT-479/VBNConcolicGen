package vbn.instrument.switches;

import soot.Unit;
import soot.Value;
import soot.jimple.Constant;
import soot.jimple.Expr;
import soot.jimple.Jimple;
import vbn.instrument.InstrumentData;

public class JimpleValueInstrument {
    public static void instrument(Value v, Value left, Unit unit, InstrumentData data) {
        if (v instanceof Constant) {
            var typeSwitch = new ValueTypeSwitch(data, unit, v);
            v.getType().apply(typeSwitch);
            var pushConstant = data.runtime.getMethod("void pushConstant(java.lang.Object)");
            var invokeStmt = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(pushConstant.makeRef(), typeSwitch.v));
            data.units.insertBefore(invokeStmt, unit);
            return;
        }
        if (!(v instanceof Expr)) {
            v.apply(new RightReferenceSwitch(data, unit));
            return;
        }
        // Dealing with expression
        var expressionSwitch = new ExpressionSwitch(data, unit);
        v.apply(expressionSwitch);
    }
}
