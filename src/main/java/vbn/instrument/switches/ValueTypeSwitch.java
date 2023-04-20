package vbn.instrument.switches;

import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import vbn.instrument.InstrumentData;

public class ValueTypeSwitch extends TypeSwitch<Object> {
    public InstrumentData data;
    public Value v;
    public Unit unit;
    public Value boxLocal;
    public Unit assignStmt;

    public ValueTypeSwitch(InstrumentData data, Unit unit, Value v) {
        this.data = data;
        this.v = v;
        this.unit = unit;
        this.boxLocal = v;
    }

    public void caseIntType(IntType t) {
        makeBoxedValue(v, "int", "java.lang.Integer");
    }

    public void caseDoubleType(DoubleType t) {
        makeBoxedValue(v, "double", "java.lang.Double");
    }

    public void caseBooleanType(BooleanType t) {
        makeBoxedValue(v, "boolean", "java.lang.Boolean");
    }

    public void caseByteType(ByteType t) {
        makeBoxedValue(v, "byte", "java.lang.Byte");
    }

    public void caseCharType(CharType t) {
        makeBoxedValue(v, "char", "java.lang.Char");
    }

    public void caseFloatType(FloatType t) {
        makeBoxedValue(v, "float", "java.lang.Float");
    }

    public void caseLongType(LongType t) {
        makeBoxedValue(v, "long", "java.lang.Long");
    }

    public void caseShortType(ShortType t) {
        makeBoxedValue(v, "short", "java.lang.Short");
    }

    public void caseRefType(RefType t) {}

    @Override
    public void caseArrayType(ArrayType t) {}

    public void makeBoxedValue(Value v, String type, String boxedType) {
        var boxMethod = Scene.v().getMethod(String.format("<%s: %s valueOf(%s)>", boxedType, boxedType, type));
        Local boxLocal = Jimple.v().newLocal(String.format("box%d", data.body.getLocalCount()), RefType.v(boxedType));
        this.boxLocal = boxLocal;
        data.body.getLocals().add(boxLocal);
        var expr = Jimple.v().newStaticInvokeExpr(boxMethod.makeRef(), v);
        assignStmt = Jimple.v().newAssignStmt(boxLocal, expr);
    }
}
