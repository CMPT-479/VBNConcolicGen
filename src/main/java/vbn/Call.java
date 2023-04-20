package vbn;

import lombok.NonNull;
import vbn.state.*;
import vbn.state.constraints.IOperand;
import vbn.state.helpers.ComputeConstraints;
import vbn.state.helpers.ComputeValueType;
import vbn.state.value.Value;

import javax.annotation.Nullable;

import static vbn.state.helpers.ComputeOperand.*;

/**
 *
 */

public class Call {
    static State globalState;

    static ComputeConstraints computeConstraints;

    /**
     * Hello World just to test involving a function
     */
    public static void helloWorld() {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * When the program begins
     */
    @SuppressWarnings("unused")
    public static void init() {
        // Create new to avoid carried over state
        globalState = new State();
        computeConstraints = new ComputeConstraints();

        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * Handle any assignment that we don't control the value of
     * e.g. user input, external functions, etc.
     */
//    public static void initNewInput(String symName, Object value) {
////        var valueType = computeType.get(value);
////        globalState.addSymbol(symName, valueType, value);
//    }

    /**
     * Push symbols used in the computation.
     * The left operand is pushed first for binary operations.
     */
    @SuppressWarnings("unused")
    public static void pushSym(@NonNull String symName, Object value) {
        @Nullable
        var result = globalState.getSymbolCanBeNull(symName);

        if (result == null) {
            Value.Type type = ComputeValueType.getType(value);
            result = globalState.addSymbol(symName, type, value);
        }
        else {
            globalState.updateSymbolConcreteValue(symName, value);
        }

        computeConstraints.pushSymbol(result);
    }

    /**
     *
     * @param value the concrete value to store
     */
    @SuppressWarnings("unused")
    public static void pushConstant(Object value) {
    }

    /**
     * Applies an Operand expressed as a string
     * @param op the untrimmed string
     */
    @SuppressWarnings("unused")
    public static void apply(String op){
        var opTrimmed = op.trim();

        var binOp = getBinaryOperand(opTrimmed);
        if (binOp != null) {
            applyOperand(binOp);
        }

        var unaOp = getUnaryOperand(opTrimmed);
        if (unaOp != null) {
            applyOperand(unaOp);
        }

        throw new RuntimeException("Tried processing an op that does not exist");
    }

    /**
     * To cast an object to a type
     * @param typeToCast the type to cast the symbol into
     */
    public static void applyCast(String typeToCast) {

    }

    /**
     * Select the operand used for computation
     * @param operand the operand (e.g. + or -) to be applied to the symbols
     * @param <JEnum> the type of operand
     */
    private static <JEnum extends IOperand> void applyOperand(JEnum operand) {
        computeConstraints.setOperand(operand);
    }

    /**
     * Store the result of this operand in the constraints
     * We decided not to update the value of symName, using a strategy of "Update on Use"
     * @param symName the name of the symbol to store the expression
     */
    @SuppressWarnings("unused")
    public static void finalizeStore(String symName, Object object) {
        var constraint = computeConstraints.generateFromPushes(globalState.getSymbol(symName));
        globalState.pushConstraint(constraint);
    }

    /**
     * Store the result of this operand in the constraints
     */
    @SuppressWarnings("unused")
    public static void finalizeIf(int lineNumber) {
        var constraint = computeConstraints.generateFromPushes();
    }

    /**
     * When the DFS search hits an error, return, etc.
     */
    @SuppressWarnings("unused")
    public static void terminatePath(int lineNumber) {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * Trigger before involving a function
     */
    public static void beforeInvokeFunc() {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * Trigger after involving a function
     */
    public static void afterInvokeFunc() {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    /**
     * After the program is completed
     */
    public static void terminatedWithError() {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }
}

