package vbn.instrument.switches;

import com.google.protobuf.Value;
import soot.Local;
import soot.Type;
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
        left.apply(new ReferenceSwitch(data, stmt, "finalizeStore", false));
    }

    public void caseInvokeStmt(InvokeStmt stmt) {
        // Handle method invocation statements
        var invokeExpr = stmt.getInvokeExpr();
        ExpressionInstrumentUtil.invoke(invokeExpr, stmt, data);
    }

    public void caseIfStmt(IfStmt stmt) {
        // Handle if statements
        var condition = stmt.getCondition();
        if (!(condition instanceof BinopExpr)) return;
        JimpleValueInstrument.instrument(condition, null, stmt, data);
    }

    public void caseIdentityStmt(IdentityStmt stmt) {
        var right = stmt.getRightOp();
        if (right instanceof ParameterRef || right instanceof ThisRef) {
            stmt.getLeftOp().apply(new ReferenceSwitch(data, stmt, "popStore", true));
        }
    }

    public void caseLookupSwitchStmt(LookupSwitchStmt stmt) {
    }

    public void caseReturnStmt(ReturnStmt stmt) {

    }

    public void caseReturnVoidStmt(ReturnVoidStmt stmt) {
    }

    public void caseTableSwitchStmt(TableSwitchStmt stmt) {
    }

}
