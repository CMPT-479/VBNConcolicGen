package vbn.instrument;

import vbn.instrument.switches.StatementSwitch;
import soot.*;
import soot.util.Chain;

import java.util.Iterator;
import java.util.Map;

public class Instrument extends BodyTransformer {
    public SymbolTable symbolTable;
    public SootClass runtime;

    private static final Instrument instance = new Instrument();
    public Instrument() {
        symbolTable = new SymbolTable();
        runtime = Scene.v().loadClassAndSupport("vbn.Call");
    }

    @Override
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        var method = body.getMethod();
        System.out.println("\n---instrument method : " + method.getSignature());
        Chain<Unit> units = body.getUnits();
        Iterator<Unit> it = units.snapshotIterator();

        var statementSwitch = new StatementSwitch(new InstrumentData(units, body, symbolTable, runtime, body.getClass().getName()));
        while (it.hasNext()) {
            var unit = it.next();
            System.out.println("\t"+unit);
            unit.apply(statementSwitch);
        }

        it = units.snapshotIterator();
        System.out.println("---after instrument");
        while (it.hasNext()) {
            var unit = it.next();
            System.out.println("\t" + unit);
        }
    }
}
