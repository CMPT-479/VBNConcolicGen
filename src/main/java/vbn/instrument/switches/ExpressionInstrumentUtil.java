package vbn.instrument.switches;

import soot.RefType;
import soot.Unit;
import soot.jimple.*;
import vbn.instrument.InstrumentData;
import vbn.instrument.InstrumentResult;

import java.util.ArrayList;
import java.util.List;

public class ExpressionInstrumentUtil {
    public static InstrumentResult invoke(InvokeExpr expr, InstrumentData data) {
        var invokeMethod = expr.getMethod();
        if (!invokeMethod.getDeclaringClass().getName().equals(data.mainClass)) return new InstrumentResult();
        var args = expr.getArgs();
        // Push argument in reverse order
        var result = new InstrumentResult();
        for (int i = args.size() - 1; i >= 0; i--) {
            var argumentSwitch = new PushSwitch(data);
            args.get(i).apply(argumentSwitch);
            result.combine(argumentSwitch.getResult());
        }
        if (expr instanceof InstanceInvokeExpr) {
            var argumentSwitch = new PushSwitch(data);
            ((InstanceInvokeExpr) expr).getBase().apply(argumentSwitch);
            result.combine(argumentSwitch.getResult());
        }
        var funBegin = data.runtime.getMethod("void beforeInvokeFunc()").makeRef();
        var funEnd = data.runtime.getMethod("void afterInvokeFunc()").makeRef();
        var beginStmt = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(funBegin));
        var endStmt = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(funEnd));
        result.beforeUnits.add(beginStmt);
        result.afterUnits.add(endStmt);
        return result;
    }

    public static void length(LengthExpr expr, InstrumentData data) {
    }

    public static InstrumentResult arithmetic(BinopExpr expr, InstrumentData data) {
        var left = expr.getOp1();
        var right = expr.getOp2();
        if (left instanceof Constant && right instanceof Constant) return new InstrumentResult();
        var leftResult = JimpleValueInstrument.instrument(left, null, data);
        var rightResult = JimpleValueInstrument.instrument(right, null, data);
        var result = InstrumentResult.combine(leftResult, rightResult);
        var apply = data.runtime.getMethod("apply", List.of(RefType.v("java.lang.String"))).makeRef();
        var symbol = StringConstant.v(expr.getSymbol());
        var applyStmt = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(apply, symbol));
        result.beforeUnits.add(applyStmt);
        return result;
    }

    public static InstrumentResult comparison(BinopExpr expr, InstrumentData data) {
        var left = expr.getOp1();
        var right = expr.getOp2();
        if (left instanceof Constant && right instanceof Constant) return new InstrumentResult();
        var leftResult = JimpleValueInstrument.instrument(left, null, data);
        var rightResult = JimpleValueInstrument.instrument(right, null, data);
        var result = InstrumentResult.combine(leftResult, rightResult);
        var apply = data.runtime.getMethod("apply", List.of(RefType.v("java.lang.String"))).makeRef();
        var symbol = StringConstant.v(expr.getSymbol());
        var applyStmt = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(apply, symbol));
        result.beforeUnits.add(applyStmt);
        return result;
    }

    public static InstrumentResult logical(BinopExpr expr, InstrumentData data) {
        var left = expr.getOp1();
        var right = expr.getOp2();
        if (left instanceof Constant && right instanceof Constant) return new InstrumentResult();
        var leftResult = JimpleValueInstrument.instrument(left, null, data);
        var rightResult = JimpleValueInstrument.instrument(right, null, data);
        var result = InstrumentResult.combine(leftResult, rightResult);
        var apply = data.runtime.getMethod("apply", List.of(RefType.v("java.lang.String"))).makeRef();
        var symbol = StringConstant.v(expr.getSymbol());
        var applyStmt = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(apply, symbol));
        result.beforeUnits.add(applyStmt);
        return result;
    }

}
