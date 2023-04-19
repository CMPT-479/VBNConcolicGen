package vbn.instrument.old_switches;

import vbn.instrument.InstrumentData;
import soot.*;
import soot.jimple.*;

import java.util.LinkedList;

public class CallAddressSwitch extends AbstractJimpleValueSwitch<Boolean> {
    public static class Setting {
        boolean insertBefore;
        String methodName;
        SootMethodRef methodRef;

        public Setting(boolean insertBefore, String methodName) {
            this.insertBefore = insertBefore;
        }
    }
    public InstrumentData data;
    public Unit unit;
    public Setting setting;
    public SootMethodRef methodRef;

    public CallAddressSwitch(InstrumentData data, Unit unit, Setting setting) {
        this.data = data;
        this.unit = unit;
        this.setting = setting;
    }

    public void caseLocal(Local v) {
        var mr = Scene.v().getMethod("<org.cmpt479.Runtime: void "+setting.methodName+"(int,int)>").makeRef();
        var v1 = IntConstant.v(0);
    }

    private void instrument(SootMethodRef mr, Value v1, Value v2) {
        LinkedList<Value> args = new LinkedList<>();
        if(setting.insertBefore){
            data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr,args)),unit);
        } else {
            data.units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr,args)),unit);
        }
    }
}
