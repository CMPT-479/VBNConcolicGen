package vbn.instrument;

import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.internal.JCaughtExceptionRef;
import soot.tagkit.LineNumberTag;
import vbn.instrument.switches.StatementSwitch;
import soot.*;
import soot.util.Chain;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Instrument extends BodyTransformer {
    public SootClass runtime;
    public static int counter = 1;
    public Instrument() {
        runtime = Scene.v().loadClassAndSupport("vbn.Call");
    }

    @Override
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        var method = body.getMethod();
        System.out.println("\n---instrument method : " + method.getSubSignature());
        Chain<Unit> units = body.getUnits();
        Iterator<Unit> it = units.snapshotIterator();

        // Tag line number
        while (it.hasNext()) {
            var unit = it.next();
            unit.addTag(new LineNumberTag(counter));
            counter++;
        }

        // Instrument
        it = units.snapshotIterator();
        while (it.hasNext()) {
            var unit = it.next();
            System.out.println("\t"+unit);
            unit.apply(new StatementSwitch(new InstrumentData(units, body, runtime, method.getDeclaringClass().getName())));
        }

        if (method.getSubSignature().equals("void main(java.lang.String[])")) {
            var init = runtime.getMethod("void init()");
            var stmt = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(init.makeRef()));
            body.getUnits().addFirst(stmt);
        }

        wrapInsideTryCatch(body);

        it = units.snapshotIterator();
        System.out.println("---after instrument");
        while (it.hasNext()) {
            var unit = it.next();
            System.out.println("\t" + unit);
        }
    }

    private void wrapInsideTryCatch(Body body) {
        if (!body.getMethod().getSubSignature().equals("void main(java.lang.String[])")) return;
        var units = body.getUnits();
        var exception = Jimple.v().newLocal("exception", RefType.v("java.lang.Throwable"));
        body.getLocals().add(exception);
        var eStmt = Jimple.v().newIdentityStmt(exception, new JCaughtExceptionRef());
        var trap = Jimple.v().newTrap(Scene.v().getSootClass("java.lang.Throwable"),
                units.getFirst(), units.getLast(), eStmt);
        body.getTraps().addLast(trap);
        body.getUnits().addLast(eStmt);
        var errorMethod = runtime.getMethod("void error()").makeRef();
        var errorCall = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(errorMethod));
        body.getUnits().addLast(errorCall);
        body.getUnits().addLast(Jimple.v().newReturnVoidStmt());
    }
}
