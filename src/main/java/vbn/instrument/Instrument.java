package vbn.instrument;

import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.tagkit.LineNumberTag;
import vbn.instrument.switches.StatementSwitch;
import soot.*;
import soot.util.Chain;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Instrument extends BodyTransformer {
    public SootClass runtime;

    public Instrument() {
        runtime = Scene.v().loadClassAndSupport("vbn.Call");
    }

    @Override
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        var method = body.getMethod();
        System.out.println("\n---instrument method : " + method.getSubSignature());
        Chain<Unit> units = body.getUnits();
        Iterator<Unit> it = units.snapshotIterator();

        while (it.hasNext()) {
            var unit = (Stmt) it.next();
            var tags = unit.getTags();
            var lineNumber = -1;
            if (!tags.isEmpty()) {
                lineNumber = ((LineNumberTag)tags.get(0)).getLineNumber();
            }
            System.out.println("\t"+unit);
            unit.apply(new StatementSwitch(new InstrumentData(
                    units, body, runtime,
                    method.getDeclaringClass().getName(),
                    lineNumber
            )));
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
