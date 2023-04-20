package vbn;

import vbn.constraints.*;
import vbn.constraints.helpers.ComputeConstraints;

import static vbn.constraints.helpers.ComputeOperands.*;

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
    public static void initNewInput(String symName, Object value) {
//        var valueType = computeType.get(value);
//        globalState.addSymbol(symName, valueType, value);
    }

    /**
     * Push symbols used in the computation.
     * The left operand is pushed first for binary operations.
     */
    public static void pushSym(String symName, Object value) {
        computeConstraints.pushSymbol(symName);
    }

    /**
     *
     * @param value the concrete value to store
     */
    public static void pushConstant(Object value) {
    }

    /**
     * Applies an Operand expressed as a string
     * @param op the untrimmed string
     */
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
     * Select the operand used for computation
     * @param operand the operand (e.g. + or -) to be applied to the symbols
     * @param <JEnum> the type of operand
     */
    private static <JEnum extends IOperand> void applyOperand(JEnum operand) {
        computeConstraints.setOperand(operand);
    }

    /**
     * Store the result of this operand in the constraints
     * @param symName the name of the symbol to store the expression
     */
    public static void finalizeStore(String symName, Object value) {
        computeConstraints.generateFromPushes(globalState, symName);
    }

    public static void finalizeStore(int objectId, int fieldId, Object value) {
        long id = objectId;
        id = (id << 32) | fieldId;
        finalizeStore(String.format("sym%d", id), value);
    }

    public static void finalizeStore(Object object, int fieldId, Object value) {
        finalizeStore(object.hashCode(), fieldId, value);
    }

    /**
     * Store the result of this operand in the constraints
     */
    public static void finalizeIf() {
        computeConstraints.generateFromPushes(globalState);
    }

    /**
     * When the DFS search hits an error, return, etc.
     */
    public static void terminatePath() {
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
    public static void error() {
        String name = new Object(){}.getClass().getEnclosingMethod().getName();
        System.out.println("From " + name);
    }

    public static void pushValue(Object o) {}

    public static void loadValue(Object o) {}

    public static void popStore(Object object, int fieldId, Object value) {
        popStore(object.hashCode(), fieldId, value);
    }

    public static void popStore(int objectId, int fieldId, Object value) {}

    public static void pop() {}

    public static void storeReturn(int objectId, int fieldId) {}

    public static void storeReturn(Object object, int fieldId) {}
}

//    /**
//     *
//     * @param o
//     */
//    public static void loadValue(Object o, Object value) {
//        System.out.println("DEBUG: loadValue is not currently in use");
//    }

//    /**
//     *
//     * @param o
//     */
//    public static void pushValue(Object o, Object value) {
//        System.out.println("DEBUG: pushValue is not currently in use");
//    }


//    /**
//     * Ensure that state does not carry over
//     */
//    public static void destroy() {
//        globalState = null;
//        tempComputeConstraints = null;
//    }
}
