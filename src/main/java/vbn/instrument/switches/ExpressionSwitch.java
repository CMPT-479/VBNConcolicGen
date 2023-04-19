package vbn.instrument.switches;

import soot.Unit;
import soot.jimple.AbstractExprSwitch;
import soot.jimple.AddExpr;
import soot.jimple.MulExpr;
import soot.jimple.SubExpr;
import vbn.instrument.Instrument;
import vbn.instrument.InstrumentData;

public class ExpressionSwitch extends AbstractExprSwitch<Boolean> {
    public InstrumentData data;
    public Unit unit;

    public ExpressionSwitch(InstrumentData data, Unit unit) {
        this.data = data;
        this.unit = unit;
        setResult(false);
    }

    public void caseAddExpr(AddExpr v) {
        ExpressionInstrumentUtil.arithmetic(v, unit, data);
        setResult(true);
    }

    @Override
    public void caseSubExpr(SubExpr v) {
        ExpressionInstrumentUtil.arithmetic(v, unit, data);
        setResult(true);
    }

    @Override
    public void caseMulExpr(MulExpr v) {
        ExpressionInstrumentUtil.arithmetic(v, unit, data);
        setResult(true);
    }
}
