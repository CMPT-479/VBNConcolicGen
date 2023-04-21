package vbn.instrument.switches;

import soot.RefType;
import soot.Unit;
import soot.jimple.*;
import vbn.instrument.InstrumentData;

import java.util.List;

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

    public void caseSubExpr(SubExpr v) {
        ExpressionInstrumentUtil.arithmetic(v, unit, data);
        setResult(true);
    }

    public void caseMulExpr(MulExpr v) {
        ExpressionInstrumentUtil.arithmetic(v, unit, data);
        setResult(true);
    }

    public void caseAndExpr(AndExpr v) {
        ExpressionInstrumentUtil.logical(v, unit, data);
        setResult(true);
    }

    public void caseCmpExpr(CmpExpr v) {
        ExpressionInstrumentUtil.comparison(v, unit, data);
        setResult(true);
    }

    public void caseCmpgExpr(CmpgExpr v) {
        ExpressionInstrumentUtil.comparison(v, unit, data);
        setResult(true);
    }

    public void caseCmplExpr(CmplExpr v) {
        ExpressionInstrumentUtil.comparison(v, unit, data);
        setResult(true);
    }

    public void caseDivExpr(DivExpr v) {
        ExpressionInstrumentUtil.arithmetic(v, unit, data);
        setResult(true);
    }

    public void caseEqExpr(EqExpr v) {
        ExpressionInstrumentUtil.comparison(v, unit, data);
        setResult(true);
    }

    public void caseNeExpr(NeExpr v) {
        ExpressionInstrumentUtil.comparison(v, unit, data);
        setResult(true);
    }

    public void caseGeExpr(GeExpr v) {
        ExpressionInstrumentUtil.comparison(v, unit, data);
        setResult(true);
    }

    public void caseGtExpr(GtExpr v) {
        ExpressionInstrumentUtil.comparison(v, unit, data);
        setResult(true);
    }

    public void caseLeExpr(LeExpr v) {
        ExpressionInstrumentUtil.comparison(v, unit, data);
        setResult(true);
    }

    public void caseLtExpr(LtExpr v) {
        ExpressionInstrumentUtil.comparison(v, unit, data);
        setResult(true);
    }

    public void caseOrExpr(OrExpr v) {
        ExpressionInstrumentUtil.logical(v, unit, data);
        setResult(true);
    }

    public void caseRemExpr(RemExpr v) {
        ExpressionInstrumentUtil.logical(v, unit, data);
        setResult(true);
    }

    public void caseNegExpr(NegExpr v) {
        var op = v.getOp();
        JimpleValueInstrument.instrument(op, null, unit, data);
        var apply = data.runtime.getMethod("apply", List.of(RefType.v("java.lang.String"))).makeRef();
        var symbol = StringConstant.v("neg");
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(apply, symbol)), unit);
    }

    public void caseCastExpr(CastExpr v) {
        JimpleValueInstrument.instrument(v.getOp(), null, unit, data);
        var apply = data.runtime.getMethod("applyCast", List.of(RefType.v("java.lang.String"))).makeRef();
        var symbol = StringConstant.v(v.getCastType().toString());
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(apply, symbol)), unit);
    }
}
