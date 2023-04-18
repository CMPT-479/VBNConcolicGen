package org.cmpt479.instrument.old_switches;

import org.cmpt479.instrument.InstrumentData;
import soot.Scene;
import soot.Unit;
import soot.Value;
import soot.jimple.*;

public class BinopExprSwitch extends AbstractExprSwitch<Boolean> {
    public InstrumentData data;
    public Unit unit;

    public BinopExprSwitch(InstrumentData data, Unit unit) {
        this.data = data;
        this.unit = unit;
    }

    private void caseArithmetic(BinopExpr expr) {
        var left = expr.getOp1();
        var right = expr.getOp2();
        if (left instanceof Constant && right instanceof Constant) return;
        var args = new ExpressionInstrument.Args(null, false);
        ExpressionInstrument.instrument(left, unit, data, args);
        ExpressionInstrument.instrument(right, unit, data, args);
        var method = Scene.v().getMethod("<org.cmpt479.RunTime: void applyOp(java.lang.string)>").makeRef();
        var symbol = StringConstant.v(expr.getSymbol());
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(method, symbol)), unit);
        setResult(true);
    }

    private void caseComparison(BinopExpr expr) {
        var left = expr.getOp1();
        var right = expr.getOp2();
        if (left instanceof Constant && right instanceof Constant) return;
        var args = new ExpressionInstrument.Args(null, true);
        ExpressionInstrument.instrument(left, unit, data, args);
        ExpressionInstrument.instrument(right, unit, data, args);
        var method = Scene.v().getMethod("<org.cmpt479.RunTime: void applyOp(java.lang.string)>").makeRef();
        var symbol = StringConstant.v(expr.getSymbol());
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(method, symbol)), unit);
        setResult(true);
    }

    public void caseNegExpr(NegExpr v) {
        ExpressionInstrument.instrument(v, unit, data, new ExpressionInstrument.Args(null, false));
        var mr = Scene.v().getMethod("<org.cmpt479.RunTime: void applyOp(java.lang.String)>").makeRef();
        Value sym = StringConstant.v("-");
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, sym)), unit);
    }

    public void caseAddExpr(AddExpr v) {
        caseArithmetic(v);
    }

    public void caseCmpExpr(CmpExpr v) {
        caseArithmetic(v);
    }

    public void caseCmpgExpr(CmpgExpr v) {
        caseArithmetic(v);
    }

    public void caseCmplExpr(CmplExpr v) {
        caseArithmetic(v);
    }

    public void caseMulExpr(MulExpr v) {
        caseArithmetic(v);
    }

    public void caseSubExpr(SubExpr v) {
        caseArithmetic(v);
    }

    public void caseAndExpr(AndExpr v) {
        caseArithmetic(v);
    }

    public void caseEqExpr(EqExpr v) {
        caseArithmetic(v);
    }

    public void caseNeExpr(NeExpr v) {
        caseArithmetic(v);
    }

    public void caseGeExpr(GeExpr v) {
        caseComparison(v);
    }

    public void caseGtExpr(GtExpr v) {
        caseComparison(v);
    }

    public void caseLeExpr(LeExpr v) {
        caseComparison(v);
    }

    public void caseLtExpr(LtExpr v) {
        caseComparison(v);
    }

    public void caseOrExpr(OrExpr v) {
        caseComparison(v);
    }

}
