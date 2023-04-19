package vbn.instrument.switches;

import soot.Unit;
import soot.Value;
import soot.jimple.Expr;
import vbn.instrument.InstrumentData;

public class JimpleValueInstrument {
    public static void instrument(Value v, Value left, Unit unit, InstrumentData data) {
        // Push address (symbolic)
        if (!(v instanceof Expr)) {
            v.apply(new ReferenceSwitch(data, unit, "pushSym", true));
        }
        // Push concrete value
        if (!(v instanceof Expr)) {
            v.getType().apply(new ValueTypeSwitch(data, unit, v, true));
            return;
        }
        // Dealing with expression
        var expressionSwitch = new ExpressionSwitch(data, unit);
        v.apply(expressionSwitch);
        // if (expressionSwitch.getResult()) return;
        // TODO: Handle other cases
    }
}
