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
        if (!(left instanceof Local)) return;
        Local localLeft = (Local) left;
        var mr = data.runtime.getMethod("void handleAssignment(java.lang.String)").makeRef();
        var leftSym = StringConstant.v(localLeft.getName());
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, leftSym)), stmt);
    }

    public void caseInvokeStmt(InvokeStmt stmt) {
        // Handle method invocation statements

    }

    public void caseIfStmt(IfStmt stmt) {
        // Handle if statements

    }

    public void caseIdentityStmt(IdentityStmt stmt) {
        System.out.println("Identity: " + stmt);
        var right = stmt.getRightOp();
        if (right instanceof ParameterRef || right instanceof ThisRef) {
            System.out.println("  right: " + right);
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
