package vbn.instrument.switches;

import soot.Unit;
import soot.tagkit.LineNumberTag;
import vbn.instrument.InstrumentData;
import soot.jimple.*;
import vbn.instrument.InstrumentResult;

import java.util.List;

public class StatementSwitch extends AbstractStmtSwitch<Object> {
    InstrumentData data;

    public StatementSwitch(InstrumentData data) {
        super();
        this.data = data;
    }

    public void caseAssignStmt(AssignStmt stmt) {
        // Handle assignment statements

        var left = stmt.getLeftOp();
        var right = stmt.getRightOp();
        var result = JimpleValueInstrument.instrument(right, left, data);
        var lineNumber = ((LineNumberTag) stmt.getTag("LineNumberTag")).getLineNumber();
        boolean isReturn = false;
        if (right instanceof InvokeExpr) {
            var popSwitch = new PopSwitch(data);
            left.apply(popSwitch);
            result.combine(popSwitch.getResult());
            isReturn = true;
        }
        var leftSwitch = new LeftReferenceSwitch(data, lineNumber, isReturn);
        left.apply(leftSwitch);
        result.combine(leftSwitch.getResult());
        instrument(stmt, result);
    }

    public void caseInvokeStmt(InvokeStmt stmt) {
        // Handle method invocation statements
        var invokeExpr = stmt.getInvokeExpr();
        var units = ExpressionInstrumentUtil.invoke(invokeExpr, data);
        instrument(stmt, units);
    }

    public void caseIfStmt(IfStmt stmt) {
        var condition = stmt.getCondition();
        if (!(condition instanceof BinopExpr)) return;
        var result = JimpleValueInstrument.instrument(condition, null, data);
        instrument(stmt, result);
        var trueBranch = data.runtime.getMethod("void pushTrueBranch(int)").makeRef();
        var falseBranch = data.runtime.getMethod("void pushFalseBranch(int)").makeRef();
        var finalizeIf = data.runtime.getMethod("void finalizeIf(int)").makeRef();
        var lineNumber = IntConstant.v(((LineNumberTag) stmt.getTag("LineNumberTag")).getLineNumber());
        var trueStmt = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(trueBranch, lineNumber));
        var falseStmt = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(falseBranch, lineNumber));
        var ifStmt = Jimple.v().newIfStmt(condition, trueStmt);
        var finalizeStmt = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(finalizeIf, lineNumber));
        List<Unit> units = List.of(
                ifStmt, falseStmt,
                Jimple.v().newGotoStmt(finalizeStmt),
                trueStmt, finalizeStmt
        );
        data.units.insertBefore(units, stmt);
    }

    public void caseIdentityStmt(IdentityStmt stmt) {
        if (data.body.getMethod().isConstructor()) return;
        var right = stmt.getRightOp();
        if (!(right instanceof ParameterRef || right instanceof ThisRef)) return;
        var popSwitch = new PopSwitch(data);
        stmt.getLeftOp().apply(popSwitch);
        data.units.insertBefore(popSwitch.getResult().afterUnits, data.bodyBegin);
    }

    public void caseReturnStmt(ReturnStmt stmt) {
        var pushSwitch = new PushSwitch(data);
        stmt.getOp().apply(pushSwitch);
        instrument(stmt, pushSwitch.getResult());
    }

    public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
        if (!data.body.getMethod().getSubSignature().equals("void main(java.lang.String[])")) return;
        var terminate = data.runtime.getMethod("void terminatePath(int)").makeRef();
        var lineNumber = ((LineNumberTag) stmt.getTag("LineNumberTag")).getLineNumber();
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(terminate, IntConstant.v(lineNumber))), stmt);
    }

    public void instrument(Unit u, InstrumentResult result) {
        data.units.insertBefore(result.beforeUnits, u);
        data.units.insertAfter(result.afterUnits, u);
    }
}
