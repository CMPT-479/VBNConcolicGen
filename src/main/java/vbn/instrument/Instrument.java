package vbn.instrument;

import soot.jimple.Jimple;
import vbn.instrument.switches.StatementSwitch;
import soot.*;
import soot.util.Chain;

import java.util.Iterator;
import java.util.List;
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
        System.out.println("\n---instrument method : " + method.getSubSignature());
        Chain<Unit> units = body.getUnits();
        Iterator<Unit> it = units.snapshotIterator();

        var statementSwitch = new StatementSwitch(new InstrumentData(units, body, symbolTable, runtime, body.getClass().getName()));
        while (it.hasNext()) {
            var unit = it.next();
            System.out.println("\t"+unit);
            unit.apply(statementSwitch);
        }

        if (method.getSubSignature().equals("void main(java.lang.String[])")) {
            var init = runtime.getMethod("void init()");
            var stmt = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(init.makeRef()));
            body.getUnits().addFirst(stmt);
        }

        it = units.snapshotIterator();
        System.out.println("---after instrument");
        while (it.hasNext()) {
            var unit = it.next();
            System.out.println("\t" + unit);
        }
    }
}
