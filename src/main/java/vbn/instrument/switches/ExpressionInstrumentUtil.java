package vbn.instrument.switches;

import soot.RefType;
import soot.Unit;
import soot.jimple.*;
import vbn.instrument.InstrumentData;

import java.util.List;

public class ExpressionInstrumentUtil {

    public static void invoke(InvokeExpr expr, Unit unit, InstrumentData data) {
        var invokeMethod = expr.getMethod();
        if (!invokeMethod.getDeclaringClass().getName().equals(data.mainClass)) return;
        addPopMethod(data, unit);
    }

    public static void length(LengthExpr expr, Unit unit, InstrumentData data) {
        addPopMethod(data, unit);
    }

    public static void arithmetic(BinopExpr expr, Unit unit, InstrumentData data) {
        var left = expr.getOp1();
        var right = expr.getOp2();
        if (left instanceof Constant && right instanceof Constant) return;
        JimpleValueInstrument.instrument(left, null, unit, data);
        JimpleValueInstrument.instrument(right, null, unit, data);
        var apply = data.runtime.getMethod("apply", List.of(RefType.v("java.lang.String"))).makeRef();
        var symbol = StringConstant.v(expr.getSymbol());
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(apply, symbol)), unit);
    }

    public static void comparison(BinopExpr expr, Unit unit, InstrumentData data) {
        var left = expr.getOp1();
        var right = expr.getOp2();
        if (left instanceof Constant && right instanceof Constant) return;
        JimpleValueInstrument.instrument(left, null, unit, data);
        JimpleValueInstrument.instrument(right, null, unit, data);
        var apply = data.runtime.getMethod("void apply(java.lang.String)").makeRef();
        var symbol = StringConstant.v(expr.getSymbol());
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(apply, symbol)), unit);
    }

    public static void logical(BinopExpr expr, Unit unit, InstrumentData data) {
        var left = expr.getOp1();
        var right = expr.getOp2();
        if (left instanceof Constant && right instanceof Constant) return;
        JimpleValueInstrument.instrument(left, null, unit, data);
        JimpleValueInstrument.instrument(right, null, unit, data);
        var apply = data.runtime.getMethod("void apply(java.lang.String)").makeRef();
        var symbol = StringConstant.v(expr.getSymbol());
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(apply, symbol)), unit);
    }

    public static void addPopMethod(InstrumentData data, Unit unit) {
        var pop = data.runtime.getMethod("pop", List.of()).makeRef();
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(pop)), unit);
    }

}
