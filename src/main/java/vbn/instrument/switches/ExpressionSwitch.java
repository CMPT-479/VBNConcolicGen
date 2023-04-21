package vbn.instrument.switches;

import soot.RefType;
import soot.jimple.*;
import vbn.instrument.InstrumentData;
import vbn.instrument.InstrumentResult;

import java.util.List;

public class ExpressionSwitch extends AbstractJimpleValueSwitch<InstrumentResult> {
    public InstrumentData data;

    public ExpressionSwitch(InstrumentData data) {
        this.data = data;
        setResult(new InstrumentResult());
    }

    public void caseAddExpr(AddExpr v) {
        getResult().combine(ExpressionInstrumentUtil.arithmetic(v, data));
    }

    public void caseSubExpr(SubExpr v) {
        getResult().combine(ExpressionInstrumentUtil.arithmetic(v, data));
    }

    public void caseMulExpr(MulExpr v) {
        getResult().combine(ExpressionInstrumentUtil.arithmetic(v, data));
    }

    public void caseAndExpr(AndExpr v) {
        getResult().combine(ExpressionInstrumentUtil.logical(v, data));
    }

    public void caseCmpExpr(CmpExpr v) {
        getResult().combine(ExpressionInstrumentUtil.comparison(v, data));
    }

    public void caseCmpgExpr(CmpgExpr v) {
        getResult().combine(ExpressionInstrumentUtil.comparison(v, data));
    }

    public void caseCmplExpr(CmplExpr v) {
        getResult().combine(ExpressionInstrumentUtil.comparison(v, data));
    }

    public void caseDivExpr(DivExpr v) {
        getResult().combine(ExpressionInstrumentUtil.arithmetic(v, data));
    }

    public void caseEqExpr(EqExpr v) {
        getResult().combine(ExpressionInstrumentUtil.comparison(v, data));
    }

    public void caseNeExpr(NeExpr v) {
        getResult().combine(ExpressionInstrumentUtil.comparison(v, data));
    }

    public void caseGeExpr(GeExpr v) {
        getResult().combine(ExpressionInstrumentUtil.comparison(v, data));
    }

    public void caseGtExpr(GtExpr v) {
        getResult().combine(ExpressionInstrumentUtil.comparison(v, data));
    }

    public void caseLeExpr(LeExpr v) {
        getResult().combine(ExpressionInstrumentUtil.comparison(v, data));
    }

    public void caseLtExpr(LtExpr v) {
        getResult().combine(ExpressionInstrumentUtil.comparison(v, data));
    }

    public void caseOrExpr(OrExpr v) {
        getResult().combine(ExpressionInstrumentUtil.logical(v, data));
    }

    public void caseRemExpr(RemExpr v) {
        getResult().combine(ExpressionInstrumentUtil.logical(v, data));
    }

    public void caseNegExpr(NegExpr v) {
        var op = v.getOp();
        setResult(JimpleValueInstrument.instrument(op, null, data));
        var apply = data.runtime.getMethod("apply", List.of(RefType.v("java.lang.String"))).makeRef();
        var symbol = StringConstant.v("neg");
        getResult().beforeUnits.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(apply, symbol)));
    }
}
