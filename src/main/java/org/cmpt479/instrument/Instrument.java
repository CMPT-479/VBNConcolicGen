package org.cmpt479.instrument;

import org.cmpt479.instrument.switches.StatementSwitch;
import soot.*;
import soot.jimple.IfStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.TableSwitchStmt;
import soot.util.Chain;

import java.util.Iterator;
import java.util.Map;

public class Instrument extends BodyTransformer {
    public SymbolTable symbolTable;
    public Instrument() {
        symbolTable = new SymbolTable();
        // Scene.v().loadClassAndSupport("org.cmpt479.Call");
    }

    @Override
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        // body's method
        var method = body.getMethod();
        System.out.println("\ninstrumenting method : " + method.getSignature());
        Chain<Unit> units = body.getUnits();
        Iterator<Unit> it = units.snapshotIterator();

        var statementSwitch = new StatementSwitch(new InstrumentData(units, body, symbolTable));
        while (it.hasNext()) {
            var unit = it.next();
            unit.apply(statementSwitch);
        }

    }
}
