package org.cmpt479.instrument.switches;

import org.cmpt479.instrument.InstrumentData;
import soot.*;
import soot.jimple.*;

public class StatementSwitch extends AbstractStmtSwitch<Object> {
    InstrumentData data;

    public StatementSwitch(InstrumentData data) {
        super();
        this.data = data;
    }

    public void caseAssignStmt(AssignStmt stmt) {
        // Handle assignment statements

    }

    public void caseInvokeStmt(InvokeStmt stmt) {
        // Handle method invocation statements

    }

    public void caseIfStmt(IfStmt stmt) {
        // Handle if statements

    }

    public void caseIdentityStmt(IdentityStmt stmt) {

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
