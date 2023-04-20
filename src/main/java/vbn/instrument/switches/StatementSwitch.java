package vbn.instrument.switches;

import soot.tagkit.LineNumberTag;
import vbn.instrument.InstrumentData;
import soot.jimple.*;

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
        JimpleValueInstrument.instrument(right, left, stmt, data);
        left.apply(new LeftReferenceSwitch(data, stmt));
    }

    public void caseInvokeStmt(InvokeStmt stmt) {
        // Handle method invocation statements
        var invokeExpr = stmt.getInvokeExpr();
        ExpressionInstrumentUtil.invoke(invokeExpr, stmt, data);
    }

    public void caseIfStmt(IfStmt stmt) {
        var condition = stmt.getCondition();
        if (!(condition instanceof BinopExpr)) return;
        JimpleValueInstrument.instrument(condition, null, stmt, data);
        var finalizeIf = data.runtime.getMethod("void finalizeIf(int)").makeRef();
        var lineNumberTag = ((LineNumberTag) stmt.getTag("LineNumberTag")).getLineNumber();
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(finalizeIf, IntConstant.v(lineNumberTag))), stmt);
    }

    public void caseIdentityStmt(IdentityStmt stmt) {

    }

    public void caseReturnStmt(ReturnStmt stmt) {

    }

    public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
        if (!data.body.getMethod().getSubSignature().equals("void main(java.lang.String[])")) return;
        var terminate = data.runtime.getMethod("void terminatePath(int)").makeRef();
        var lineNumber = ((LineNumberTag) stmt.getTag("LineNumberTag")).getLineNumber();
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(terminate, IntConstant.v(lineNumber))), stmt);
    }
}
