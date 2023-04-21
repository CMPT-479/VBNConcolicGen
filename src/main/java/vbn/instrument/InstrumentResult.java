package vbn.instrument;

import soot.Unit;

import java.util.ArrayList;
import java.util.List;

public class InstrumentResult {
    public List<Unit> beforeUnits, afterUnits;

    public InstrumentResult() {
        beforeUnits = new ArrayList<>();
        afterUnits = new ArrayList<>();
    }

    public void combine(InstrumentResult second) {
        beforeUnits.addAll(second.beforeUnits);
        afterUnits.addAll(second.afterUnits);
    }

    public static InstrumentResult combine(InstrumentResult first, InstrumentResult second) {
        var result = new InstrumentResult();
        result.beforeUnits.addAll(first.beforeUnits);
        result.beforeUnits.addAll(second.beforeUnits);
        result.afterUnits.addAll(first.afterUnits);
        result.afterUnits.addAll(second.afterUnits);
        return result;
    }
}
