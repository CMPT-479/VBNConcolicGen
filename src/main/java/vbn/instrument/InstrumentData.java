package vbn.instrument;

import soot.Body;
import soot.Unit;
import soot.util.Chain;

public class InstrumentData {
    public Chain<Unit> units;
    public Body body;
    public SymbolTable symbolTable;

    public InstrumentData(Chain<Unit> units, Body body, SymbolTable symbolTable) {
        this.units = units;
        this.body = body;
        this.symbolTable = symbolTable;
    }
}
