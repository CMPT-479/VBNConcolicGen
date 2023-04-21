package vbn.instrument.switches;

import soot.RefType;
import soot.SootMethod;
import soot.Value;
import soot.jimple.Jimple;
import soot.jimple.StringConstant;
import vbn.instrument.InstrumentData;

import java.util.List;

public class PopSwitch extends AbstractReferenceSwitch {
    SootMethod method;

    public PopSwitch(InstrumentData data) {
        super(data);
        method = data.runtime.getMethod("popArg", List.of(
                RefType.v("java.lang.String"),
                RefType.v("java.lang.Object")
        ));
    }

    @Override
    void instrument(String symbol, Value value) {
        var typeSwitch = new ValueTypeSwitch(data, value);
        value.getType().apply(typeSwitch);
        getResult().afterUnits.addAll(typeSwitch.getResult());
        var caller = Jimple.v().newStaticInvokeExpr(method.makeRef(), StringConstant.v(symbol), typeSwitch.v);
        getResult().afterUnits.add(Jimple.v().newInvokeStmt(caller));
    }
}
