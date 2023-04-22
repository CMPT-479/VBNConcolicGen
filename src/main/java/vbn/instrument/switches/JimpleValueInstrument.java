package vbn.instrument.switches;

import soot.IntType;
import soot.RefType;
import soot.Unit;
import soot.Value;
import soot.jimple.Constant;
import soot.jimple.Expr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import vbn.instrument.InstrumentData;
import vbn.instrument.InstrumentResult;

import java.util.List;

public class JimpleValueInstrument {
    public static InstrumentResult instrument(Value v, Value left, InstrumentData data) {
        if (!(v instanceof Expr)) {
            var refSwitch = new RightReferenceSwitch(data);
            v.apply(refSwitch);
            return refSwitch.getResult();
        } else {
            var exprSwitch = new ExpressionSwitch(data);
            v.apply(exprSwitch);
            return exprSwitch.getResult();
        }
    }
}
