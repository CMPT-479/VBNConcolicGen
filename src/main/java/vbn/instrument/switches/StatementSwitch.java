package vbn.instrument.switches;

import com.google.protobuf.Value;
import soot.Local;
import soot.Type;
import vbn.instrument.InstrumentData;
import soot.jimple.*;

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
        JimpleValueInstrument.instrument(right, left, stmt, data);
        left.apply(new ReferenceSwitch(data, stmt, "finalizeStore", false));
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
        var finalizeIf = data.runtime.getMethod("void finalize()").makeRef();
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(finalizeIf)), stmt);
    }

    public void caseIdentityStmt(IdentityStmt stmt) {
        var right = stmt.getRightOp();
        if (right instanceof ParameterRef || right instanceof ThisRef) {
            stmt.getLeftOp().apply(new ReferenceSwitch(data, stmt, "popStore", true));
        }
    }

    public void caseReturnStmt(ReturnStmt stmt) {

    }

    public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
        if (!data.body.getMethod().getSubSignature().equals("void main(java.lang.String[])")) return;
        var terminate = data.runtime.getMethod("void terminatePath()").makeRef();
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(terminate)), stmt);
    }
}
