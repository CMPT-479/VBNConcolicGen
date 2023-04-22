package vbn.instrument;

import soot.Body;
import soot.SootClass;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;
import soot.util.Chain;

public class InstrumentData {
    public Chain<Unit> units;
    public Body body;
    public SootClass runtime;
    public String mainClass;
    public Unit bodyBegin;
    public InstrumentData(Chain<Unit> units, Body body, SootClass runtime, String mainClass) {
        this.units = units;
        this.body = body;
        this.runtime = runtime;
        this.mainClass = mainClass;
        refresh();
    }

    void refresh() {
        var it = units.snapshotIterator();
        Unit begin = body.getUnits().getFirst();
        while (it.hasNext()) {
            var unit = it.next();
            begin = unit;
            if (!(unit instanceof IdentityStmt)) break;
            var idStmt = (IdentityStmt) unit;
            var right = idStmt.getRightOp();
            if (!(right instanceof ParameterRef || right instanceof ThisRef)) break;
        }
        bodyBegin = begin;
    }

}
