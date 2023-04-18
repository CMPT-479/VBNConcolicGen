package vbn.instrument.old_switches;

import vbn.instrument.InstrumentData;
import soot.*;
import soot.jimple.Jimple;

public class ValueSwitch extends TypeSwitch<Boolean> {
    public static class Setting {
        public boolean insertBefore;
        public boolean conditional;
        public String call;

        public Setting(boolean insertBefore, boolean conditional, String call) {
            this.insertBefore = insertBefore;
            this.conditional = conditional;
            this.call = call;
        }
    }


    public InstrumentData data;
    public Value v;
    public Unit unit;
    public Setting setting;


    public ValueSwitch(InstrumentData data, Value v, Unit unit, Setting setting) {

        this.data = data;
        this.v = v;
        this.unit = unit;
        this.setting = setting;
    }


    public void caseArrayType(ArrayType t) {
        if (setting.conditional) instrument(RefType.v("java.lang.Object"), "java.lang.Object");
        else instrument();
    }

    public void caseBooleanType(BooleanType t) {
        instrument(t, "boolean");
    }

    public void caseByteType(ByteType t) {
        instrument(t, "byte");
    }

    public void caseCharType(CharType t) {
        instrument(t, "char");
    }

    public void caseDoubleType(DoubleType t) {
        instrument(t, "double");
    }

    public void caseFloatType(FloatType t) {
        instrument(t, "float");
    }

    public void caseIntType(IntType t) {
        instrument(t, "int");
    }

    public void caseLongType(LongType t) {
        instrument(t, "long");
    }

    public void caseRefType(RefType t) {
        if (setting.conditional) instrument(RefType.v("java.lang.Object"), "java.lang.Object");
        else instrument();
    }

    public void caseShortType(ShortType t) {
        super.caseShortType(t);
    }

    public void caseStmtAddressType(StmtAddressType t) {
        super.caseStmtAddressType(t);
    }

    @Override
    public void caseNullType(NullType t) {
        instrument(RefType.v("java.lang.Object"), "java.lang.Object");
    }

    private void instrument(Type t, String type) {
        SootMethodRef mr = Scene.v().getMethod("<org.cmpt479.Runtime: void "+setting.call+"("+type+")>").makeRef();
        Local tmpLocal = Jimple.v().newLocal("__ct_"+data.body.getLocalCount(), t);
        data.body.getLocals().add(tmpLocal);
        var as = Jimple.v().newAssignStmt(tmpLocal, v);
        if(setting.insertBefore){
            data.units.insertBefore(as,unit);
            data.units.insertBefore(Jimple.v().newInvokeStmt(
                    Jimple.v().newStaticInvokeExpr(mr,tmpLocal)),unit);
        } else {
            data.units.insertAfter(Jimple.v().newInvokeStmt(
                    Jimple.v().newStaticInvokeExpr(mr,tmpLocal)),unit);
            data.units.insertAfter(as,unit);
        }
    }

    private void instrument() {
        SootMethodRef mr = Scene.v().getMethod("<org.cmpt.479.Runtime: void loadValue()>").makeRef();
        data.units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr)), unit);
    }

}
