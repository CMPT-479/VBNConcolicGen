package vbn.instrument;

import vbn.instrument.switches.StatementSwitch;
import soot.*;
import soot.util.Chain;

import java.util.Iterator;
import java.util.Map;

public class Instrument extends BodyTransformer {
    public SymbolTable symbolTable;
    public SootClass runtime;
    public Instrument() {
        symbolTable = new SymbolTable();
        runtime = Scene.v().loadClassAndSupport("vbn.Call");
    }

    @Override
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        // body's method
        var method = body.getMethod();
        System.out.println("\ninstrumenting method : " + method.getSignature());
        Chain<Unit> units = body.getUnits();
        Iterator<Unit> it = units.snapshotIterator();

        var statementSwitch = new StatementSwitch(new InstrumentData(units, body, symbolTable, runtime));
        while (it.hasNext()) {
            var unit = it.next();
            System.out.println("\t"+unit);
        }

    }
}
