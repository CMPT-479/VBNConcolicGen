package vbn.instrument.old_switches;

import vbn.instrument.InstrumentData;
import soot.*;
import soot.jimple.*;

import java.util.LinkedList;

public class CallAddressSwitch extends AbstractJimpleValueSwitch<Boolean> {
    public static class Setting {
        boolean insertBefore, definitelyAdd;
        String methodName;

        public Setting(boolean insertBefore, boolean definitelyAdd, String methodName) {
            this.insertBefore = insertBefore;
            this.definitelyAdd = definitelyAdd;
            this.methodName = methodName;
        }
    }
    public InstrumentData data;
    public Unit unit;
    public Setting setting;

    public CallAddressSwitch(InstrumentData data, Unit unit, Setting setting) {
        this.data = data;
        this.unit = unit;
        this.setting = setting;
    }

    public void caseLocal(Local v) {
        var mr = Scene.v().getMethod("<org.cmpt479.Runtime: void "+setting.methodName+"(int,int)>").makeRef();
        var v1 = IntConstant.v(0);
        var v2 = IntConstant.v(data.symbolTable.get(v.getName()));
        instrument(mr, v1, v2);
    }

    public void caseArrayRef(ArrayRef v) {
        var mr = Scene.v().getMethod("<org.cmpt479.Runtime: void "+setting.methodName+"(java.lang.Object,int)>").makeRef();
        var v1 = v.getBase();
        var v2 = v.getIndex();
        instrument(mr, v1, v2);

    }

    public void caseStaticFieldRef(StaticFieldRef v) {
        var mr = Scene.v().getMethod("<org.cmpt479.Runtime: void "+setting.methodName+"(int,int)>").makeRef();
        var v1 = IntConstant.v(data.symbolTable.get(v.getField().getDeclaringClass().getName()));
        var v2 = IntConstant.v(data.symbolTable.get(v.getField().getName()));
        instrument(mr, v1, v2);
    }

    public void caseInstanceFieldRef(InstanceFieldRef v) {
        var mr = Scene.v().getMethod("<org.cmpt479.Runtime: void "+setting.methodName+"(java.lang.Object,int)>").makeRef();
        var v1 = v.getBase();
        var v2 = IntConstant.v(data.symbolTable.get(v.getField().getName()));
        instrument(mr, v1, v2);
    }

    @Override
    public void defaultCase(Object obj) {
        if (!setting.definitelyAdd) return;
        var mr = Scene.v().getMethod("<org.cmpt479.Runtime: void "+setting.methodName+"(int,int)>").makeRef();
        var v1 = IntConstant.v(0);
        var v2 = IntConstant.v(0);
        instrument(mr, v1, v2);
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
