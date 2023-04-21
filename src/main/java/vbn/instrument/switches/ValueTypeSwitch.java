package vbn.instrument.switches;

import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.Jimple;
import vbn.instrument.InstrumentData;

import java.util.ArrayList;
import java.util.List;

public class ValueTypeSwitch extends TypeSwitch<List<Unit>> {
    public InstrumentData data;
    public Value v;
    public ValueTypeSwitch(InstrumentData data, Value v) {
        this.data = data;
        this.v = v;
        setResult(new ArrayList<>());
    }

    public void caseIntType(IntType t) {
        super.caseIntType(t);
        makeBoxedValue("int", "java.lang.Integer");
    }

    public void caseDoubleType(DoubleType t) {
        super.caseDoubleType(t);
        makeBoxedValue("double", "java.lang.Double");
    }

    public void caseBooleanType(BooleanType t) {
        super.caseBooleanType(t);
        makeBoxedValue("boolean", "java.lang.Boolean");
    }

    public void caseByteType(ByteType t) {
        super.caseByteType(t);
        makeBoxedValue("byte", "java.lang.Byte");
    }

    public void caseCharType(CharType t) {
        super.caseCharType(t);
        makeBoxedValue("char", "java.lang.Char");
    }

    public void caseFloatType(FloatType t) {
        super.caseFloatType(t);
        makeBoxedValue("float", "java.lang.Float");
    }

    public void caseLongType(LongType t) {
        super.caseLongType(t);
        makeBoxedValue("long", "java.lang.Long");
    }

    public void caseShortType(ShortType t) {
        super.caseShortType(t);
        makeBoxedValue("short", "java.lang.Short");
    }

    public void makeBoxedValue(String type, String boxedType) {
        var boxMethod = Scene.v().getMethod(String.format("<%s: %s valueOf(%s)>", boxedType, boxedType, type));
        Local local = Jimple.v().newLocal(String.format("box%d", data.body.getLocalCount()), RefType.v(boxedType));
        data.body.getLocals().add(local);
        var expr = Jimple.v().newStaticInvokeExpr(boxMethod.makeRef(), v);
        var assignment = Jimple.v().newAssignStmt(local, expr);
        getResult().add(assignment);
        this.v = local;
    }

    public void defaultCase(Type t) {
        if (v instanceof Constant) return;
        var local = Jimple.v().newLocal(String.format("tmp%d", data.body.getLocalCount()), t);
        data.body.getLocals().add(local);
        var assignment = Jimple.v().newAssignStmt(local, v);
        getResult().add(assignment);
        this.v = local;
    }
}
