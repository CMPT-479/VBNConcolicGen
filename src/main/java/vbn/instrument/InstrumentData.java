package vbn.instrument;

import soot.Body;
import soot.SootClass;
import soot.Unit;
import soot.util.Chain;

public class InstrumentData {
    public Chain<Unit> units;
    public Body body;
    public SymbolTable symbolTable;
    public SootClass runtime;
    public String mainClass;

    public InstrumentData(Chain<Unit> units, Body body, SymbolTable symbolTable, SootClass runtime, String mainClass) {
        this.units = units;
        this.body = body;
        this.symbolTable = symbolTable;
        this.runtime = runtime;
        this.mainClass = mainClass;
    }

}
