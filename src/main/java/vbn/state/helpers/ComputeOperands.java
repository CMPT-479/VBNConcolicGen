package vbn.state.constraints.helpers;

import lombok.NonNull;
import soot.jimple.Jimple;
import vbn.state.constraints.BinaryOperand;
import vbn.state.constraints.UnaryOperand;

import javax.annotation.Nullable;
import java.util.Map;

public class ComputeOperands {

    static final Map<String, BinaryOperand> binaryEnumsMap = Map.ofEntries(
            Map.entry("&&", BinaryOperand.AND),
            Map.entry("||", BinaryOperand.OR),
            Map.entry("==", BinaryOperand.EQ),

            Map.entry("+", BinaryOperand.ADD),
            Map.entry("-", BinaryOperand.MINUS),
            Map.entry("*", BinaryOperand.MULTIPLY),
            Map.entry("/", BinaryOperand.DIVIDE),

            Map.entry("<", BinaryOperand.LT),
            Map.entry("<=", BinaryOperand.LTE),
            Map.entry(">", BinaryOperand.GT),
            Map.entry(">=", BinaryOperand.GTE)
    );
    static final Map<String, UnaryOperand> unaryEnumsMap = Map.ofEntries(
            Map.entry(Jimple.NEG, UnaryOperand.NEG)
    );

    @Nullable
    public static BinaryOperand getBinaryOperand(@NonNull String operandStr) {
        return binaryEnumsMap.get(operandStr);
    }

    @Nullable
    public static UnaryOperand getUnaryOperand(@NonNull String operandStr) {
        return unaryEnumsMap.get(operandStr);
    }
}
