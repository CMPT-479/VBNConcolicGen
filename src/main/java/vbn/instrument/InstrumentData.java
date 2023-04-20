package vbn.instrument;

import soot.Body;
import soot.SootClass;
import soot.Unit;
import soot.util.Chain;

public class InstrumentData {
    public Chain<Unit> units;
    public Body body;
    public SootClass runtime;
    public String mainClass;

    public InstrumentData(Chain<Unit> units, Body body, SootClass runtime, String mainClass) {
        this.units = units;
        this.body = body;
        this.runtime = runtime;
        this.mainClass = mainClass;
    }

}
