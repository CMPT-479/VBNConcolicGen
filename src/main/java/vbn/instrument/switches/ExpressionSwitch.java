package vbn.instrument.switches;

import soot.Unit;
import soot.jimple.AbstractExprSwitch;
import soot.jimple.AddExpr;
import vbn.instrument.Instrument;
import vbn.instrument.InstrumentData;

public class ExpressionSwitch extends AbstractExprSwitch<Object> {
    public InstrumentData data;
    public Unit unit;

    public ExpressionSwitch(StatementSwitch statementSwitch, Unit unit) {
        data = statementSwitch.data;
        this.unit = unit;
    }

    public void caseAddExpr(AddExpr v) {
        ExpressionInstrumentUtil.arithmetic(v, unit, data);
    }
}
